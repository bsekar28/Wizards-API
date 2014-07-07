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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 * Represents a Wizard Framework, i.e, JDialog with card panel layout and back,
 * next/finish buttons
 * <p>
 * WizardPages can be added to it using <code>addPage(WizardPage)</code> methods
 * which accept instances of {@link WizardPage}. To set the title for the Wizard
 * use the <code> setTitle(String) </code>, to set the title bar description use
 * the <code>setTitleBar(String) </code> object, to set the title image make use
 * of the <code>setTitleImage</code>
 * </p>
 **/

@SuppressWarnings("rawtypes")
public class Wizard {

	private JPanel cardPanel = null;
	private CardLayout cardLayout = null;
	protected JButton backButton = null;
	protected JButton nextButton = null;
	protected JButton cancelButton = null;
	private JDialog wizard = null;
	private WizardPage current = null;
	private JLabel titleLabel = null;
	private JLabel descriptionLabel = null;
	private String wizardTitle;
	private boolean isFinished = false;

	private ArrayList<WizardPage> wizardPages = null;

	public Wizard(JFrame owner) {

		wizard = new JDialog(owner);
		wizard.setSize(600, 500);
		wizard.setLocationRelativeTo(null);
		BufferedImage image = new BufferedImage(1, 1,
				BufferedImage.TYPE_4BYTE_ABGR_PRE);
		wizard.setIconImage(image);
		wizard.setModalityType(ModalityType.TOOLKIT_MODAL);
		wizard.setResizable(false);

		initComponents();
	}

	private void initComponents() {
		JPanel buttonPanel;
		JPanel titlePanel;
		JPanel titleParentPanel;

		buttonPanel = new JPanel();
		titlePanel = new JPanel();

		Box buttonBox = new Box(BoxLayout.X_AXIS);

		cardPanel = new JPanel();
		cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);

		backButton = new JButton("Back");
		nextButton = new JButton("Next");
		cancelButton = new JButton("Cancel");
		titleLabel = new JLabel();
		descriptionLabel = new JLabel();

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wizard.setVisible(false);
				wizard.dispatchEvent(new WindowEvent(wizard,
						WindowEvent.WINDOW_CLOSED));
			}
		});

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (current == null)
					return;

				if (current.getPageState() == WizardPageState.Last) {

					if (!current.finishUp())
						return;

					isFinished = true;
					wizard.setVisible(false);
					wizard.dispatchEvent(new WindowEvent(wizard,
							WindowEvent.WINDOW_CLOSED));
					return;
				}

				if (!current.moveNext())
					return;

				current = current.getNextPage();
				setButtonState(current);
				setTitleBar(current);
			}
		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (current == null)
					return;

				if (current.getPageState() == WizardPageState.Head) {
					wizard.setVisible(false);
					return;
				}

				if (!current.moveBack())
					return;

				current = current.getPreviousPage();
				setButtonState(current);
				setTitleBar(current);
			}
		});

		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

		buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
		buttonBox.add(backButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(nextButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(cancelButton);
		buttonPanel.setPreferredSize(new Dimension(500, 40));
		buttonPanel.add(buttonBox, BorderLayout.EAST);

		titleParentPanel = new JPanel();
		titleParentPanel.setLayout(new BorderLayout());
		titleParentPanel.add(new JSeparator(), BorderLayout.SOUTH);
		titleParentPanel.setPreferredSize(new Dimension(500, 80));

		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(null);

		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		titleLabel.setBounds(20, 10, 200, 27);
		titlePanel.add(titleLabel);

		descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		descriptionLabel.setBounds(20, 40, 400, 27);
		titlePanel.add(descriptionLabel);

		titleParentPanel.add(titlePanel, BorderLayout.CENTER);
		wizard.getContentPane().add(titleParentPanel,
				java.awt.BorderLayout.NORTH);
		wizard.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
		wizard.getContentPane().add(cardPanel, java.awt.BorderLayout.CENTER);
	}

	public void show() {

		showFirstPage();
		wizard.setVisible(true);

	}

	public void addPage(WizardPage page) {

		if (wizardPages == null)
			wizardPages = new ArrayList<WizardPage>();

		wizardPages.add(page);

		page.setVisible(false);
		cardPanel.add(page);
	}

	private void showFirstPage() {

		WizardPage firstPage = null;

		for (int index = 0; index < wizardPages.size(); index++) {

			if (wizardPages.get(index).getPageState() != WizardPageState.Head)
				continue;

			firstPage = wizardPages.get(index);
			break;
		}

		if (!hasWizardPage(firstPage))
			cardPanel.add(firstPage);

		firstPage.setVisible(true);
		setButtonState(firstPage);
		setTitleBar(firstPage);

		current = firstPage;
	}

	private Boolean hasWizardPage(WizardPage page) {
		Component[] components = cardPanel.getComponents();

		for (int index = 0; index < components.length; index++)
			if (components[index] == page)
				return true;

		return false;
	}

	private void setButtonState(WizardPage page) {

		switch (page.getPageState()) {
		case Head:
			backButton.setVisible(false);
			nextButton.setVisible(true);
			nextButton.setText("Next");
			break;
		case Middle:
			backButton.setVisible(true);
			nextButton.setVisible(true);
			nextButton.setText("Next");
			break;
		case Last:
			backButton.setVisible(true);
			nextButton.setVisible(true);
			nextButton.setText("Finish");
			break;
		default:
			break;
		}
	}

	public String getTitle() {
		return wizardTitle;
	}

	public void setTitle(String wizardTitle) {
		this.wizardTitle = wizardTitle;
	}

	private void setTitleBar(WizardPage page) {
		titleLabel.setText(page.getTitle());
		descriptionLabel.setText(page.getDescription());
		wizard.setTitle(this.getTitle() + " - " + titleLabel.getText());
	}

	public void setIconImage(Image image) {
		wizard.setIconImage(image);
	}

	public boolean checkIsFinished() {
		return isFinished;
	}
}
