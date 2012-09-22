package com.nilhcem.xebia.essentials.cards.list;

/*protected*/ interface IOnCardItemSelected {
	void onCardsListItemSelected(int position);

	/**
	 * Must be in the Ui thread.
	 */
	void onCardsFinishedLoading();
}
