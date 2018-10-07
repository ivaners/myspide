package tk.ivanlily.common;


import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

import java.util.Map;

/**
 * 自定义消息编码器
 */
public class ObjectSerializer implements Serializer<Object> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    /**
     * 序列化
     */
    @Override
    public byte[] serialize(String topic, Object data) {
        return SerializationUtils.serialize(data);
    }

    @Override
    public void close() {

    }
}
