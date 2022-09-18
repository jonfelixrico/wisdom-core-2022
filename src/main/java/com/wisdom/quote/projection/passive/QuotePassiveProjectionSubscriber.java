package com.wisdom.quote.projection.passive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ResolvedEvent;

@Service
class QuotePassiveProjectionSubscriber implements Subscriber<ResolvedEvent> {

	@Override
	public void onSubscribe(Subscription s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNext(ResolvedEvent t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub

	}

}
