package com.yoti.robohoover.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoti.robohoover.exception.InvalidInputException;
import com.yoti.robohoover.service.Position;
import com.yoti.robohoover.service.RobotHooverServiceImpl;
import com.yoti.robohoover.service.RobotHooverServiceRequest;
import com.yoti.robohoover.service.RobotHooverServiceResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class RobotHooverControllerTest {
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    private static final String REQUEST_JSON = "{\"roomSize\" : [5, 5],\"coords\" : [1, 2], \"patches\" : [[1, 0], [2, 2], [2, 3]], \"instructions\" : \"NNESEESWNWW\"}";
    private static final String INVALID_ROOM_SIZE_REQUEST = "{\"roomSize\" : [-5, -5],\"coords\" : [1, 2], \"patches\" : [[1, 0]], \"instructions\" : \"NNESEESWNWW\"}";
    private static final String INVALID_HOOVER_COORDINATES_REQUEST = "{\"roomSize\" : [5, 5],\"coords\" : [-1, -2], \"patches\" : [[1, 0]], \"instructions\" : \"NNESEESWNWW\"}";
    private static final String INVALID_INSTRUCTIONS_REQUEST = "{\"roomSize\" : [5, 5],\"coords\" : [1, 2], \"patches\" : [[1, 0]], \"instructions\" : \"ANNESEESWNWW\"}";
    private static final String EXPECTED_RESPONSE = "{\"coords\":[1,3]," + "\"patches\":1" + "}";
    private MockMvc mockMvc;
    @Mock
    private RobotHooverServiceImpl robotHooverService;
    @InjectMocks
    private RobotHooverController roboHooverController;

    @Before
    public void setup() throws Exception {
        this.mockMvc = buildMvc(roboHooverController).build();
    }

    @Test
    public void shouldReturnTheResponse() throws Exception {
        when(robotHooverService.runHoover(any(RobotHooverServiceRequest.class)))
                .thenReturn(new RobotHooverServiceResponse(new Position(1, 3), 1));

        this.mockMvc.perform(post("/robohoover/process")
                .contentType("application/json;charset=UTF-8")
                .content(REQUEST_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(EXPECTED_RESPONSE))
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$.patches").value(1))
                .andExpect(jsonPath("$.coords[0]").value(1))
                .andExpect(jsonPath("$.coords[1]").value(3));

    }

    @Test
    public void shouldThrowInvalidInputException_InvalidRoomCoordinates() throws Exception {
        this.mockMvc.perform(post("/robohoover/process")
                .contentType("application/json;charset=UTF-8")
                .content(INVALID_ROOM_SIZE_REQUEST))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.info").value("Invalid Room coordinates"));
    }

    @Test
    public void shouldThrowInvalidInputException_InvalidHooverCoordinates() throws Exception {
        this.mockMvc.perform(post("/robohoover/process")
                .contentType("application/json;charset=UTF-8")
                .content(INVALID_HOOVER_COORDINATES_REQUEST))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.info").value("Invalid hoover coordinates"));
    }

    @Test
    public void shouldThrowInvalidInputException_InvalidInstructions() throws Exception {
        when(robotHooverService.runHoover(any(RobotHooverServiceRequest.class)))
                .thenThrow(new InvalidInputException("Invalid instructions"));

        this.mockMvc.perform(post("/robohoover/process")
                .contentType("application/json;charset=UTF-8")
                .content(INVALID_INSTRUCTIONS_REQUEST))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.info").value("Invalid instructions"));
    }

    private StandaloneMockMvcBuilder buildMvc(Object... controllers) {
        MockitoAnnotations.initMocks(this);
        List<HttpMessageConverter<?>> converters = new LinkedList<>();
        new HttpMessageConverterHelper().addDefaults(converters);
        MappingJackson2HttpMessageConverter jsonConverter;
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                jsonConverter = (MappingJackson2HttpMessageConverter) converter;
                jsonConverter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }
        }

        HttpMessageConverter<?>[] convertersArray = converters.toArray(new HttpMessageConverter[converters.size()]);
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        return MockMvcBuilders.standaloneSetup(roboHooverController)
                .setMessageConverters(convertersArray)
                .setConversionService(conversionService)
                .setHandlerExceptionResolvers(createExceptionHandler(converters));
    }

    private ExceptionHandlerExceptionResolver createExceptionHandler(List<HttpMessageConverter<?>> converters) {
        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.registerBeanDefinition("advice", new RootBeanDefinition(ControllerExceptionHandler.class, null, null));
        exceptionHandlerExceptionResolver.setApplicationContext(applicationContext);
        exceptionHandlerExceptionResolver.setMessageConverters(converters);
        exceptionHandlerExceptionResolver.afterPropertiesSet();
        return exceptionHandlerExceptionResolver;
    }


    private class HttpMessageConverterHelper extends WebMvcConfigurationSupport {
        public void addDefaults(List<HttpMessageConverter<?>> converters) {
            addDefaultHttpMessageConverters(converters);
        }
    }
}