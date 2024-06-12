package de.ait_tr.g_40_shop.logging;


import de.ait_tr.g_40_shop.domain.dto.ProductDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class AspectLogging {

    private Logger logger = LoggerFactory.getLogger(AspectLogging.class);

    // Pointcut (срез) - по сути является правилом, определяющим, куда будет внедрена дополнительная логика

    @Pointcut("execution(* de.ait_tr.g_40_shop.service.ProductServiceImpl.save(..))")
    public void saveProduct(){}


    // Advice - это дополнительная логика, которая и будет внедрена к основной
    // Before - это адвайс, логика которого внедряется перед основной логикой
    @Before("saveProduct()")
    public void beforeSavingProduct(){
        logger.info("Method save of the class ProductServiceImpl called");

    }

    // Вариант метода с перехватом входящего входящего параметра
    @Before("saveProduct()")
    public void beforeSavingProduct(JoinPoint joinPoint){
        Object[] params = joinPoint.getArgs();
        logger.info("Method save of the class ProductServiceImpl called with parameter {}", params[0]);

    }

//    // After - это адвайс логика которого внедряется после выполнения метода
    @After("saveProduct()")
    public void afterSavingProduct(){
        logger.info("Method save of the class ProductServiceImpl finished his its work");

    }

    @Pointcut("execution(* de.ait_tr.g_40_shop.service.ProductServiceImpl.getById(..))")
    public void getProductById(){}


    @AfterReturning("getProductById()")
    public void afterReturningProduct(){
        logger.info("Method get by Id of the class ProductServiceImpl successfully returned result");
    }

    @AfterThrowing("getProductById()")
    public void afterThrowingIfProductNotFound(){
        logger.info("Method getById of the class ProductServiceImpl threw an exception");
    }


    // Вариант предыдущего адвайса, который позволяет работать с объектом,
    // который вернул целевой метод
    @AfterReturning(pointcut = "getProductById()",
    returning = "result")
    public void afterReturningProduct(Object result){
        logger.info("Method getById of the class ProductServiceImpl successfully returned result : {}", result);
    }

    // Вариант предыдущего адвайса, который позволяет работать с объектом исключения,
    // который выбросил целевой метод
    @AfterThrowing(pointcut = "getProductById()",
    throwing = "e")
    public void afterThrowingIfProductNotFound(Exception e){
        logger.warn("Method getById of the class ProductServiceImpl threw an exception {}", e.getMessage());
    }

    @Pointcut("execution(* de.ait_tr.g_40_shop.service.ProductServiceImpl.getAllActiveProducts(..))")
    public void getAllProducts(){}

    // Around - адвайс, который позволяет внедрить дополнительную логику вокруг целевой логики, то есть
    //          и до, и после, и даже вместо целевой логики, подменяя её действительный результат.
    // Здесь мы внедряем логику и до, и после целевой логики, а также перехватываем фактический результат,
    // который возвращает целевой метод и применяем к списку продуктов дополнительную фильтрацию плюсом
    // к той фильтрации, которую производит сам целевой метод.
    // Также имеем возможность перехватывать исключения, которые выбрасывает целевой метод и обрабатывать их.


    @Around("getAllProducts()")
    public Object aroundGettingAllProducts(ProceedingJoinPoint joinPoint){
        logger.info("Method getAllActiveProducts of the class ProductServiceImpl called");

        List<ProductDto> result = null;

        try{
            result = ((List<ProductDto>) joinPoint.proceed())
                    .stream()
                    .filter(x -> x.getPrice().intValue()>100)
                    .toList();
        }catch (Throwable e){
            logger.error("Method getAllActiveProducts of the class ProductServiceImpl threw an exception: {}", e.getMessage());
        }

        logger.info("Method getAllActiveProducts of the class ProductServiceImpl finished its work with result: {}", result);
        return result;
    }

    @Pointcut("execution(* de.ait_tr.g_40_shop.service.ProductServiceImpl.*(..))")
    public void productServiceImplVoids() {}

    @Around("productServiceImplVoids()")
    public Object aroundVoidsProductServiceImpl(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        Object result = null;

        logger.info("Method {} of the class ProductServiceImpl called with args: {}", methodName, methodArgs);

        try{
            result = joinPoint.proceed();
        }catch (Throwable e){
            logger.info("Method {} of the class ProductServiceImpl threw an exception: {}",methodName, e.getMessage());
        }

        logger.info("Method {} of the class ProductServiceImpl finished its work", methodName);
        return result;
    }

//    @Pointcut("execution(* de.ait_tr.g_40_shop.service.*(..)")
//    public void allServiceImplVoids(){}
//
//    @Around("allServiceImplVoids()")
//    public Object aroundVoidsServiceImpl(ProceedingJoinPoint joinPoint){
//        String methodName = joinPoint.getSignature().getName();
//        String className = joinPoint.getSignature().getClass().getSimpleName();
//        Object[] params = joinPoint.getArgs();
//        Object result = null;
//
//        logger.info("Method {} of the class {} called with args: {}", methodName,className, params);
//
//        try{
//            result = joinPoint.proceed();
//            if (result != null){
//                logger.info("Method {} of the class {} finished with result: {}", methodName, className, result);
//            }
//        }catch (Throwable e){
//            logger.info("Method {} of the class {} threw an exception: {}", methodName, className, e.getMessage());
//        }
//
//
//        logger.info("Method {} of the class {} finished its work", methodName, className);
//
//        return result;
//    }
}
