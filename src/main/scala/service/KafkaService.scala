package service

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters.IterableHasAsScala
import scala.jdk.javaapi.CollectionConverters.asJavaCollection

class KafkaService {
  def run() = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.243:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    val topic = "myTopic"
    val record = new ProducerRecord[String, String](topic, "key", "Hello World")
    producer.send(record)
    producer.close()

    val properties = new Properties()
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.243:9092")
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer")
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])

    val kafkaConsumer = new KafkaConsumer[String, String](properties)
    kafkaConsumer.subscribe(asJavaCollection(List(topic)))

    class MyThread extends Thread{
      override def run(): Unit = {
        while (true) {
          val results = kafkaConsumer.poll(Duration.ofSeconds(2)).asScala
          for (result <- results) {
            println("topic: " + result.topic() + " data: " + result.value())
          }
        }
      }
    }

    new MyThread().start()
  }
}
