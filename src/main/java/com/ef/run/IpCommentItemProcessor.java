package com.ef.run;

import org.springframework.batch.item.ItemProcessor;

import com.ef.dto.IpDto;
import com.ef.entity.IpComment;

public class IpCommentItemProcessor implements ItemProcessor<IpDto, IpComment> {
	

	@Override
	public IpComment process(IpDto ipDto) throws Exception {
		IpComment ipComment = new IpComment(ipDto);
		return ipComment;
	}

}
