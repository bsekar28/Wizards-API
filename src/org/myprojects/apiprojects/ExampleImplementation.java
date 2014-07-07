package org.myprojects.apiprojects;

import java.awt.Image;

import javax.swing.JFrame;

/**
 * This examples gives the overview of how to use the wizard API
 * 
 * @author Bhuvana.Sekar
 * 
 */

public class ExampleImplementation extends Wizard {

	private ExampleWizardPage page1 = null;
	private ExampleWizardPage page2 = null;

	public ExampleImplementation(JFrame owner, Context context) {
		super(owner);
		initComponents(context);
	}

	private void initComponents(Context context) {

		setTitle("New Wizard");

		page1 = new ExampleWizardPage(null, null);
		page2 = new ExampleWizardPage(WizardPageState.Last, null, null);
		
		page1.setContext(context);
		page2.setContext(context);

		page1.setTitle("Example page 1");
		page2.setTitle("Example page 2");

		page1.setDescription("page 1: Description");
		page2.setDescription("page 2: Description");

		page1.setNextPage(page2);
		page2.setPreviousPage(page1);

		addPage(page1);
		addPage(page2);

		show();

	}
}

class Context {

}
