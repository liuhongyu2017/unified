package com.azxc.unified.common.convert;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.utils.DictUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;

/**
 * @author lhy
 * @version 1.0 2020/3/14
 */
public class JsonDictConvertSerializer extends JsonSerializer<Byte>
    implements ContextualSerializer {

  /**
   * 字典类型
   */
  private String label;

  public JsonDictConvertSerializer() {
    this("");
  }

  private JsonDictConvertSerializer(String label) {
    this.label = label;
  }

  @Override
  public void serialize(Byte aByte, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeString(DictUtil.keyValue(label, StrUtil.toString(aByte)));
  }

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider serializerProvider,
      BeanProperty beanProperty) throws JsonMappingException {
    JsonSerializer<?> result = serializerProvider.findNullValueSerializer(beanProperty);
    // property 不能为空，property 类型必须为 Byte
    if (beanProperty != null &&
        ObjectUtil.equal(beanProperty.getType().getRawClass(), Byte.class)) {
      JsonDictConvert jsonDictConvert = beanProperty.getAnnotation(JsonDictConvert.class);
      if (jsonDictConvert == null) {
        jsonDictConvert = beanProperty.getContextAnnotation(JsonDictConvert.class);
      }
      if (jsonDictConvert != null) {
        result = new JsonDictConvertSerializer(jsonDictConvert.value());
      } else {
        result = serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
      }
    }
    return result;
  }
}
