package com.yibing;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


public class JsonTest {
    enum Status {
        ACTIVE(1), INACTIVE(2);

        private int status;

        Status(int status) {
            this.status = status;
        }
    }


    static class Item {
        private String content;

        Item(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "content='" + content + '\'' +
                    '}';
        }
    }

    static class Wrapper {
        private Item item;
        private Status status;

        public Wrapper() {
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Wrapper{" +
                    "item=" + item +
                    ", status=" + status +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        String s = "{\"item\":\"content1234\", \"status\":\"ACTIVE\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Wrapper wrapper = objectMapper.readValue(s, Wrapper.class);
        System.out.println(wrapper);
    }
}
