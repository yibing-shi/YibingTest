package com.yshi.toy.robot;

import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;

import java.util.concurrent.atomic.AtomicReference;

public abstract class MockTest {

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    public static class SetStateAnswer<T> implements Answer<T> {
        private final AtomicReference<T> reference;
        private final T expectedValue;

        public SetStateAnswer(T expectedValue) {
            reference = new AtomicReference<T>();
            this.expectedValue = expectedValue;
        }

        public T getActualValue() {
            return reference.get();
        }

        @Override
        public T answer(InvocationOnMock invocation) throws Throwable {
            reference.set(expectedValue);
            return expectedValue;
        }
    }
}
