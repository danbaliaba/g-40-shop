package de.ait_tr.g_40_shop.exception_handling;


import de.ait_tr.g_40_shop.exception_handling.exceptions.FourthTestException;
import de.ait_tr.g_40_shop.exception_handling.exceptions.ThirdTestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    // 3 способ обработки ошибок
    // ПЛЮС -  мы получаем глобальный обработчик ошибок, который обрабатывает
    //         ошибки, выбрасываемые в любом месте нашего проекта, то есть нам
    //         не требуется писать такие обработчики в разных местах проекта.
    //         Также удобство в том, что вся логика обработки ошибок
    //         сконцентрирована в одном месте - в этом классе-адвайсе
    // МИНУС - данный способ нам не подходит, если нам требуется различная
    //         обработка одного и того же исключения для разных контроллеров
    @ExceptionHandler(ThirdTestException.class)
    public ResponseEntity<Response> handleException(ThirdTestException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FourthTestException.class)
    public ResponseEntity<Response> handleException(FourthTestException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
