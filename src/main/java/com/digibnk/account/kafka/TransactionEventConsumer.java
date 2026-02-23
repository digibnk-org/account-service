package com.digibnk.account.kafka;

import com.digibnk.account.event.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumes TransactionCreatedEvents from the digibnk.transaction.created topic.
 *
 * In this architecture the balances are already updated synchronously via Feign.
 * This consumer demonstrates the EVENT-DRIVEN / eventually-consistent pattern
 * that you would use in a pure async Saga where Feign is removed.
 *
 * Current role: audit logging + extensibility hook (notifications, analytics, etc.)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventConsumer {

    @KafkaListener(
            topics = "${app.kafka.topics.transaction-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onTransactionCreated(
            @Payload TransactionCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Received TransactionCreatedEvent | topic={} partition={} offset={} | " +
                 "ref={} type={} amount={} status={}",
                topic, partition, offset,
                event.getReference(), event.getType(),
                event.getAmount(), event.getStatus());

    }
}
