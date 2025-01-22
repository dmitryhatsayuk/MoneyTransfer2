package ru.netology.moneytransfer2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoneyTransfer2Application {

    public static void main(String[] args) {
        Card card1 = new Card("1111111111111111", "11/25", "111");
        Card card2 = new Card("2222222222222222", "12/25", "222");
        card1.fill("RUR", 99999);

        SpringApplication.run(MoneyTransfer2Application.class, args);
    }

}