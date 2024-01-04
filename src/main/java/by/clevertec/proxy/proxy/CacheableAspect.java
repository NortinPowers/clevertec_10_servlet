package by.clevertec.proxy.proxy;

import by.clevertec.proxy.cache.Cache;
import by.clevertec.proxy.data.InfoProductDto;
import by.clevertec.proxy.data.ProductDto;
import by.clevertec.proxy.entity.Product;
import by.clevertec.proxy.exception.ProductNotFoundException;
import by.clevertec.proxy.mapper.ProductMapper;
import by.clevertec.proxy.repository.ProductRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class CacheableAspect {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final Cache<UUID, Object> cache;

    @SuppressWarnings("checkstyle:IllegalCatch")
    @Around("@annotation(by.clevertec.proxy.proxy.Cacheable) && execution(* by.clevertec.proxy.service.impl.ProductServiceImpl.get(..))")
    public Object cacheableGet(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        UUID uuid = (UUID) args[0];
        if (cache.get(uuid) != null) {
            return cache.get(uuid);
        }
        InfoProductDto result;
        try {
            result = (InfoProductDto) joinPoint.proceed();
        } catch (ProductNotFoundException e) {
            throw new ProductNotFoundException(uuid);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(uuid, result);
        return result;
    }

    @AfterReturning(pointcut = "@annotation(by.clevertec.proxy.proxy.Cacheable) && execution(* by.clevertec.proxy.service.ProductService.create(..))", returning = "uuid")
    public void cacheableCreate(UUID uuid) {
        Optional<Product> optionalProduct = productRepository.findById(uuid);
        optionalProduct.ifPresent(product -> cache.put(uuid, mapper.toInfoProductDto(product)));
    }

    @AfterReturning(pointcut = "@annotation(by.clevertec.proxy.proxy.Cacheable) && execution(* by.clevertec.proxy.service.ProductService.delete(..)) && args(uuid)", argNames = "uuid")
    public void cacheableDelete(UUID uuid) {
        cache.remove(uuid);
    }

    @AfterReturning(pointcut = "@annotation(by.clevertec.proxy.proxy.Cacheable) && execution(* by.clevertec.proxy.service.ProductService.update(..)) && args(uuid, productDto)", argNames = "uuid, productDto")
    public void cacheableUpdate(UUID uuid, ProductDto productDto) {
        Product product = productRepository.findById(uuid).orElseThrow(() -> new ProductNotFoundException(uuid));
        cache.put(uuid, mapper.toInfoProductDto(product));
    }
}
