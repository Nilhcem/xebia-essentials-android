package com.nilhcem.xebia.essentials.bo;

import android.content.Context;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;
import com.nilhcem.xebia.essentials.dao.CardDao;
import com.nilhcem.xebia.essentials.model.Card;

@EBean(scope = Scope.Singleton)
public class CardService extends AbstractService<Card, CardDao> {
	@RootContext
	protected Context mContext;

	protected CardService() {
	}

	@AfterInject
	protected void setDao() {
		this.setDao(mContext, Card.class);
	}
}
