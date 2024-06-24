package com.example.springboilerplate.config;

import com.example.springboilerplate.dto.comment.CommentDTO;
import com.example.springboilerplate.entity.Comment;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        // LocalDateTime to String 변환
        Converter<LocalDateTime, String> toStringDate = new Converter<>() {
            @Override
            public String convert(MappingContext<LocalDateTime, String> context) {
                return context.getSource() != null ? context.getSource().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            }
        };
        // Comment 엔티티를 CommentDTO 로 매핑
        modelMapper.addMappings(new PropertyMap<Comment, CommentDTO>() {
            @Override
            protected void configure() {
                using(toStringDate).map(source.getCreatedAt()).setCreatedAt(null);
                using(toStringDate).map(source.getUpdatedAt()).setUpdatedAt(null);
                map().setUuid(source.getId().getUuid());
                map().setUserId(source.getUser().getUserId());
                map().setBoardId(source.getBoard().getBoardId());
                map().setContent(source.getContent());
            }
        });

        return modelMapper;
    }
}