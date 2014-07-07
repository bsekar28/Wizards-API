/*******************************************************************************
 * This file is part of Wizard API.
 * Wizard API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Wizard API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Wizard API.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *  Bhuvana Sekar - initial implementation and API
 *****************************************************************************/
package org.myprojects.apiprojects;

import javax.swing.JPanel;

/**
 * Represents a WizardPage, i.e, a page view in the wizard which populates the
 * context information of it in a generic object T <code>setContext(T)</code>
 * which can be retrieved using <code> T getContext()</code>
 * <p>
 * Each WizardPage has a state (Head/ Middle/ Last) a previous and next page in
 * case of a middle WizardPage, which can be set using
 * <code>setPreviousPage(WizardPage<T>)</code> and
 * <code>setNextPage(WizardPage<T>)</code> or while creating a WizardPage using
 * the constructor
 * <code> WizardPage(WizardPageState, WizardPage previous, WizardPage next)</code>
 * </p>
 **/

@SuppressWarnings("rawtypes")
public abstract class WizardPage<T> extends JPanel {

	private static final long serialVersionUID = 1L;
	private WizardPage previousPage = null;
	private WizardPage nextPage = null;
	private WizardPageState pageState = WizardPageState.Middle;
	private T context = null;
	private String title = null;
	private String description = null;

	public WizardPage(WizardPageState pageState, WizardPage previousPage,
			WizardPage nextPage) {
		this.pageState = pageState;
		this.previousPage = previousPage;
		this.nextPage = nextPage;
	}

	public WizardPageState getPageState() {
		return pageState;
	}

	public WizardPage getPreviousPage() {
		return previousPage;
	}

	public WizardPage getNextPage() {
		return nextPage;
	}

	public void setPreviousPage(WizardPage previousPage) {
		this.previousPage = previousPage;
	}

	public void setNextPage(WizardPage nextPage) {
		this.nextPage = nextPage;
	}

	public Boolean moveNext() {

		if (nextPage == null)
			return false;

		if (!validatePage())
			return false;

		updateContext();

		setVisible(false);
		nextPage.setVisible(true);

		return true;
	}

	public Boolean moveBack() {

		if (previousPage == null)
			return false;

		setVisible(false);
		previousPage.setVisible(true);

		return true;
	}

	public Boolean finishUp() {
		if (pageState != WizardPageState.Last)
			System.out
					.println("finishUp can be invoked only on the last wizard page.");

		if (!validatePage())
			return false;

		updateContext();

		return true;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setContext(T context) {
		this.context = context;
	}

	public T getContext() {
		return this.context;
	}

	protected abstract void updateContext();

	protected abstract Boolean validatePage();
}
