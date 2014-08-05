package com.digitalgeko.mobileapptraining.webservice;

import java.util.NoSuchElementException;

public abstract class Try<T> {

	public abstract T get();
	public abstract Throwable getThrowable();
	public abstract Boolean isFailure();
	public abstract Boolean isSuccess();
	
	public static class Success<T> extends Try<T> {

		private T value;
		
		public Success(T value) {
			this.value = value;
		}
		
		@Override
		public T get() {
			return value;
		}
		
		@Override
		public Throwable getThrowable() {
			return null;
		}

		@Override
		public Boolean isFailure() {
			return false;
		}

		@Override
		public Boolean isSuccess() {
			return true;
		}
	}
	
	public static class Failure<T> extends Try<T> {

		private Throwable throwable;
		
		public Failure(Throwable throwable) {
			this.throwable = throwable;
		}
		
		@Override
		public T get() {
			throw new NoSuchElementException();
		}
		
		@Override
		public Throwable getThrowable() {
			return throwable;
		}
		
		@Override
		public Boolean isFailure() {
			return true;
		}
		
		@Override
		public Boolean isSuccess() {
			return false;
		}
	}
}
