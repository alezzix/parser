package com.ef.run;

import org.springframework.batch.item.ItemProcessor;

import com.ef.dto.LogDto;
import com.ef.entity.Log;

public class LogItemProcessor implements ItemProcessor<LogDto, Log> {
	

    public Log process(LogDto logDto) throws Exception {

        Log log = new Log(logDto);
        return log;
    }

}
