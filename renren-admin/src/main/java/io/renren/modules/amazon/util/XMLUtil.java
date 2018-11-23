package io.renren.modules.amazon.util;

import io.renren.modules.amazon.dto.ListOrdersResponseDto;
import io.renren.modules.amazon.dto.OrderDto;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.List;

public class XMLUtil {

    public static ListOrdersResponseDto analysisListOrdersResponse(String responseXml){
        Document doc = null;
        ListOrdersResponseDto ordersResponseDto = new ListOrdersResponseDto();
        List<OrderDto> list = new ArrayList<>();
        return ordersResponseDto;
    }
}
