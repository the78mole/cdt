/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *     Anton Leherbauer (Wind River Systems)
 *******************************************************************************/
package org.eclipse.cdt.internal.ui.preferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.cdt.ui.PreferenceConstants;
import org.eclipse.cdt.utils.ui.controls.TabFolderLayout;

import org.eclipse.cdt.internal.ui.ICHelpContextIds;
import org.eclipse.cdt.internal.ui.editor.CEditor;
import org.eclipse.cdt.internal.ui.text.CSourceViewerConfiguration;
import org.eclipse.cdt.internal.ui.text.CTextTools;
import org.eclipse.cdt.internal.ui.text.ICColorConstants;

/*
 * The page for setting the editor options.
 */
public class CEditorPreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage {

	protected final String[][] fListModel = new String[][] { { PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.MultiLine"), ICColorConstants.C_MULTI_LINE_COMMENT }, { //$NON-NLS-1$
		PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.singleLine"), ICColorConstants.C_SINGLE_LINE_COMMENT }, { //$NON-NLS-1$
		PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.keywords"), ICColorConstants.C_KEYWORD }, { //$NON-NLS-1$
		PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.builtInTypes"), ICColorConstants.C_TYPE }, { //$NON-NLS-1$
		PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.strings"), ICColorConstants.C_STRING }, { //$NON-NLS-1$
        PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.operators"), ICColorConstants.C_OPERATOR }, { //$NON-NLS-1$
        PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.braces"), ICColorConstants.C_BRACES }, { //$NON-NLS-1$            
        PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.numbers"), ICColorConstants.C_NUMBER }, { //$NON-NLS-1$            
        PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.headers"), ICColorConstants.C_HEADER }, { //$NON-NLS-1$            
		PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.others"), ICColorConstants.C_DEFAULT }, { //$NON-NLS-1$
        PreferencesMessages.getString("CEditorPreferencePage.syntaxPage.cCommentTaskTags"), PreferenceConstants.EDITOR_TASK_TAG_COLOR } //$NON-NLS-1$
	};

	protected final String[][] fAppearanceColorListModel = new String[][] { 
			{PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.matchingBracketColor"), CEditor.MATCHING_BRACKETS_COLOR, null }, //$NON-NLS-1$
			{PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.inactiveCodeColor"), CEditor.INACTIVE_CODE_COLOR, null }, //$NON-NLS-1$
	};

	private CTextTools fCTextTools;

	protected List fList;
	protected ColorSelector fForegroundColorEditor;
	protected Button fBoldCheckBox;
	protected PreviewSourceViewer fPreviewViewer;

	private CEditorHoverConfigurationBlock fCEditorHoverConfigurationBlock;
	private FoldingConfigurationBlock fFoldingConfigurationBlock;

	private List fAppearanceColorList;

	private ColorSelector fAppearanceColorEditor;

	private Button fAppearanceColorDefault;


	public CEditorPreferencePage() {
		super();
		setDescription(CUIPlugin.getResourceString("CEditorPreferencePage.description")); //$NON-NLS-1$
	}

	protected OverlayPreferenceStore.OverlayKey[] createOverlayStoreKeys() {
		ArrayList overlayKeys = new ArrayList();
		
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_MULTI_LINE_COMMENT));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_MULTI_LINE_COMMENT + "_bold")); //$NON-NLS-1$
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_SINGLE_LINE_COMMENT));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_SINGLE_LINE_COMMENT + "_bold")); //$NON-NLS-1$
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_KEYWORD));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_KEYWORD + "_bold")); //$NON-NLS-1$
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_TYPE));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_TYPE + "_bold")); //$NON-NLS-1$
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_STRING));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_STRING + "_bold")); //$NON-NLS-1$
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_DEFAULT));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_DEFAULT + "_bold")); //$NON-NLS-1$        
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_BRACES));
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_BRACES + "_bold")); //$NON-NLS-1$
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_NUMBER));
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_NUMBER + "_bold")); //$NON-NLS-1$
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_OPERATOR));
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_OPERATOR + "_bold")); //$NON-NLS-1$
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, ICColorConstants.C_HEADER));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, ICColorConstants.C_HEADER + "_bold")); //$NON-NLS-1$
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, CEditor.SUB_WORD_NAVIGATION));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, CEditor.MATCHING_BRACKETS_COLOR));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, CEditor.MATCHING_BRACKETS));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, CEditor.INACTIVE_CODE_COLOR));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, CEditor.INACTIVE_CODE_ENABLE));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, CEditor.SPACES_FOR_TABS));
		
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, PreferenceConstants.EDITOR_TASK_TAG_COLOR));
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, PreferenceConstants.EDITOR_TASK_TAG_BOLD));
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, PreferenceConstants.EDITOR_TASK_INDICATION_COLOR));
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, PreferenceConstants.EDITOR_TASK_INDICATION));
        overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, PreferenceConstants.EDITOR_TASK_INDICATION_IN_OVERVIEW_RULER));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.BOOLEAN, PreferenceConstants.ENSURE_NEWLINE_AT_EOF));
      
        OverlayPreferenceStore.OverlayKey[] keys = new OverlayPreferenceStore.OverlayKey[overlayKeys.size()];
		overlayKeys.toArray(keys);
		return keys;
	}

	public static void initDefaults(IPreferenceStore store) {

		// bug 84414: enable smart home/end handling
		// This is a hidden feature of the Eclipse TextEditor:
		// If enabled, the HOME button positions the cursor to the first
		// non-whitespace character of the line (ie. the logical start of 
		// the line). Pressing it a second time, positions the cursor to the 
		// first character of the line, ie. to the physical start of the line.
		// The END button works correspondingly for the end of the line.
		// JDT also enables this feature.
		store.setDefault(AbstractTextEditor.PREFERENCE_NAVIGATION_SMART_HOME_END, true);    

		store.setDefault(CEditor.SUB_WORD_NAVIGATION, true);
		
		store.setDefault(CEditor.MATCHING_BRACKETS, true);
		PreferenceConverter.setDefault(store, CEditor.MATCHING_BRACKETS_COLOR, new RGB(170,170,170));

		store.setDefault(CEditor.INACTIVE_CODE_ENABLE, true);
		PreferenceConverter.setDefault(store, CEditor.INACTIVE_CODE_COLOR, new RGB(224, 224, 224));

		store.setDefault(CEditor.SPACES_FOR_TABS, false);

		PreferenceConverter.setDefault(store, ICColorConstants.C_MULTI_LINE_COMMENT, new RGB(63, 127, 95));
		store.setDefault(ICColorConstants.C_MULTI_LINE_COMMENT + "_bold", false); //$NON-NLS-1$

		PreferenceConverter.setDefault(store, ICColorConstants.C_SINGLE_LINE_COMMENT, new RGB(63, 125, 95));
		store.setDefault(ICColorConstants.C_SINGLE_LINE_COMMENT + "_bold", false); //$NON-NLS-1$

		PreferenceConverter.setDefault(store, ICColorConstants.C_KEYWORD, new RGB(127, 0, 85));
		store.setDefault(ICColorConstants.C_KEYWORD + "_bold", true); //$NON-NLS-1$

		PreferenceConverter.setDefault(store, ICColorConstants.C_TYPE, new RGB(127, 0, 85));
		store.setDefault(ICColorConstants.C_TYPE + "_bold", true); //$NON-NLS-1$

		PreferenceConverter.setDefault(store, ICColorConstants.C_STRING, new RGB(42, 0, 255));
		store.setDefault(ICColorConstants.C_STRING + "_bold", false); //$NON-NLS-1$

		PreferenceConverter.setDefault(store, ICColorConstants.C_DEFAULT, new RGB(0, 0, 0));
		store.setDefault(ICColorConstants.C_DEFAULT + "_bold", false); //$NON-NLS-1$

        PreferenceConverter.setDefault(store, ICColorConstants.C_OPERATOR, new RGB(0, 0, 0));
        store.setDefault(ICColorConstants.C_OPERATOR + "_bold", false); //$NON-NLS-1$

        PreferenceConverter.setDefault(store, ICColorConstants.C_BRACES, new RGB(0, 0, 0));
        store.setDefault(ICColorConstants.C_BRACES + "_bold", false); //$NON-NLS-1$

		PreferenceConverter.setDefault(store, ICColorConstants.C_HEADER, new RGB(42, 0, 255));
		store.setDefault(ICColorConstants.C_HEADER + "_bold", false); //$NON-NLS-1$

        PreferenceConverter.setDefault(store, ICColorConstants.C_NUMBER, new RGB(0, 0, 0));
        store.setDefault(ICColorConstants.C_NUMBER + "_bold", false); //$NON-NLS-1$

	}

	/*
	 * @see PreferencePage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, ICHelpContextIds.C_EDITOR_PREF_PAGE);
	}

	protected void handleListSelection() {
		int i = fList.getSelectionIndex();
		String key = fListModel[i][1];
		RGB rgb = PreferenceConverter.getColor(fOverlayStore, key);
		fForegroundColorEditor.setColorValue(rgb);
		fBoldCheckBox.setSelection(fOverlayStore.getBoolean(key + "_bold")); //$NON-NLS-1$
	}	

	private Control createSyntaxPage(Composite parent) {

		Composite colorComposite = new Composite(parent, SWT.NULL);
		colorComposite.setLayout(new GridLayout());

		Label label = new Label(colorComposite, SWT.LEFT);
		label.setText(PreferencesMessages.getString("CEditorPreferencePage.colorPage.foreground")); //$NON-NLS-1$
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite editorComposite = new Composite(colorComposite, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		editorComposite.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		editorComposite.setLayoutData(gd);

		fList = new List(editorComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = convertHeightInCharsToPixels(5);
		fList.setLayoutData(gd);

		Composite stylesComposite = new Composite(editorComposite, SWT.NULL);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		label = new Label(stylesComposite, SWT.LEFT);
		label.setText(PreferencesMessages.getString("CEditorPreferencePage.colorPage.color")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		label.setLayoutData(gd);

		fForegroundColorEditor = new ColorSelector(stylesComposite);
		Button foregroundColorButton = fForegroundColorEditor.getButton();
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		foregroundColorButton.setLayoutData(gd);

		fBoldCheckBox = new Button(stylesComposite, SWT.CHECK);
		fBoldCheckBox.setText(PreferencesMessages.getString("CEditorPreferencePage.colorPage.bold")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		gd.horizontalAlignment = GridData.BEGINNING;
		fBoldCheckBox.setLayoutData(gd);

		label = new Label(colorComposite, SWT.LEFT);
		label.setText(PreferencesMessages.getString("CEditorPreferencePage.colorPage.preview")); //$NON-NLS-1$
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Control previewer = createPreviewer(colorComposite);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = convertWidthInCharsToPixels(80);
		gd.heightHint = convertHeightInCharsToPixels(15);
		previewer.setLayoutData(gd);

		fList.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				handleListSelection();
			}
		});

		foregroundColorButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				int i = fList.getSelectionIndex();
				String key = fListModel[i][1];

				PreferenceConverter.setValue(fOverlayStore, key, fForegroundColorEditor.getColorValue());
			}
		});

		fBoldCheckBox.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				int i = fList.getSelectionIndex();
				String key = fListModel[i][1];
				fOverlayStore.setValue(key + "_bold", fBoldCheckBox.getSelection()); //$NON-NLS-1$
			}
		});

		PlatformUI.getWorkbench().getHelpSystem().setHelp(colorComposite, ICHelpContextIds.C_EDITOR_COLORS_PREF_PAGE);

		return colorComposite;
	}

	private Control createPreviewer(Composite parent) {

		fCTextTools = CUIPlugin.getDefault().getTextTools();
		CSourceViewerConfiguration configuration = new CSourceViewerConfiguration(fCTextTools, null);
		fPreviewViewer = new PreviewSourceViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		fPreviewViewer.setPreferenceStore(CUIPlugin.getDefault().getCombinedPreferenceStore());
		fPreviewViewer.configure(configuration);
		fPreviewViewer.getTextWidget().setFont(JFaceResources.getFontRegistry().get(JFaceResources.TEXT_FONT));
		fPreviewViewer.setEditable(false);

		String content = loadPreviewContentFromFile("ColorSettingPreviewCode.txt"); //$NON-NLS-1$
		IDocument document = new Document(content);
		fCTextTools.setupCDocument(document);

		fPreviewViewer.setDocument(document);

		CSourcePreviewerUpdater.registerPreviewer(fPreviewViewer, configuration, fOverlayStore);
		return fPreviewViewer.getControl();
	}

	// sets enabled flag for a control and all its sub-tree
	protected static void setEnabled(Control control, boolean enable) {
		control.setEnabled(enable);
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			Control[] children = composite.getChildren();
			for (int i = 0; i < children.length; i++)
				setEnabled(children[i], enable);
		}
	}

	private Control createAppearancePage(Composite parent) {

		Composite behaviorComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		behaviorComposite.setLayout(layout);

		String label= PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.subWordNavigation"); //$NON-NLS-1$; 
		addCheckBox(behaviorComposite, label, CEditor.SUB_WORD_NAVIGATION, 0);

		label = PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.matchingBrackets"); //$NON-NLS-1$
		addCheckBox(behaviorComposite, label, CEditor.MATCHING_BRACKETS, 0);

		label = PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.inactiveCode"); //$NON-NLS-1$
		addCheckBox(behaviorComposite, label, CEditor.INACTIVE_CODE_ENABLE, 0);

		label = PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.tabSpace"); //$NON-NLS-1$
		addCheckBox(behaviorComposite, label, CEditor.SPACES_FOR_TABS, 0);

		label = PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.ensureNewline"); //$NON-NLS-1$
		addCheckBox(behaviorComposite, label, PreferenceConstants.ENSURE_NEWLINE_AT_EOF, 0);

		Label l = new Label(behaviorComposite, SWT.LEFT);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		gd.heightHint = convertHeightInCharsToPixels(1) / 2;
		l.setLayoutData(gd);

		l = new Label(behaviorComposite, SWT.LEFT);
		l.setText(PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.appearanceColorOptions")); //$NON-NLS-1$
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		l.setLayoutData(gd);

		Composite editorComposite = new Composite(behaviorComposite, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		editorComposite.setLayout(layout);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL);
		gd.horizontalSpan = 2;
		editorComposite.setLayoutData(gd);

		fAppearanceColorList = new List(editorComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		gd.heightHint = convertHeightInCharsToPixels(8);
		fAppearanceColorList.setLayoutData(gd);

		Composite stylesComposite = new Composite(editorComposite, SWT.NONE);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		l = new Label(stylesComposite, SWT.LEFT);
		l.setText(PreferencesMessages.getString("CEditorPreferencePage.behaviorPage.Color")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		l.setLayoutData(gd);

		fAppearanceColorEditor = new ColorSelector(stylesComposite);
		Button foregroundColorButton = fAppearanceColorEditor.getButton();
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		foregroundColorButton.setLayoutData(gd);

		SelectionListener colorDefaultSelectionListener= new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean systemDefault= fAppearanceColorDefault.getSelection();
				fAppearanceColorEditor.getButton().setEnabled(!systemDefault);
				
				int i= fAppearanceColorList.getSelectionIndex();
				String key= fAppearanceColorListModel[i][2];
				if (key != null)
					fOverlayStore.setValue(key, systemDefault);
			}
		};
		
		fAppearanceColorDefault= new Button(stylesComposite, SWT.CHECK);
		fAppearanceColorDefault.setText(PreferencesMessages.getString("CEditorPreferencePage.colorPage.systemDefault")); //$NON-NLS-1$
		gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment= GridData.BEGINNING;
		gd.horizontalSpan= 2;
		fAppearanceColorDefault.setLayoutData(gd);
		fAppearanceColorDefault.setVisible(false);
		fAppearanceColorDefault.addSelectionListener(colorDefaultSelectionListener);

		fAppearanceColorList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleAppearanceColorListSelection();
			}
		});
		foregroundColorButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int i = fAppearanceColorList.getSelectionIndex();
				String key = fAppearanceColorListModel[i][1];

				PreferenceConverter.setValue(fOverlayStore, key, fAppearanceColorEditor.getColorValue());
			}
		});
		return behaviorComposite;
	}

	private void handleAppearanceColorListSelection() {
		int i = fAppearanceColorList.getSelectionIndex();
		String key = fAppearanceColorListModel[i][1];
		RGB rgb = PreferenceConverter.getColor(fOverlayStore, key);
		fAppearanceColorEditor.setColorValue(rgb);
		updateAppearanceColorWidgets(fAppearanceColorListModel[i][2]);
	}

	private void updateAppearanceColorWidgets(String systemDefaultKey) {
		if (systemDefaultKey == null) {
			fAppearanceColorDefault.setSelection(false);
			fAppearanceColorDefault.setVisible(false);
			fAppearanceColorEditor.getButton().setEnabled(true);
		} else {
			boolean systemDefault= fOverlayStore.getBoolean(systemDefaultKey);
			fAppearanceColorDefault.setSelection(systemDefault);
			fAppearanceColorDefault.setVisible(true);
			fAppearanceColorEditor.getButton().setEnabled(!systemDefault);
		}
	}

	private Control createHeader(Composite parent) {
		String text = PreferencesMessages.getString("CEditorPreferencePage.link"); //$NON-NLS-1$
		Link link = new Link(parent, SWT.NONE);
		link.setText(text);
		link.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
				String u = event.text;
				PreferencesUtil.createPreferenceDialogOn(getShell(), u, null, null);
			}
		});

		GridData gridData= new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridData.widthHint= 150; // only expand further if anyone else requires it
		link.setLayoutData(gridData);
		return link;
	}

	/*
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {

		fCEditorHoverConfigurationBlock= new CEditorHoverConfigurationBlock(this, fOverlayStore);
		fFoldingConfigurationBlock= new FoldingConfigurationBlock(fOverlayStore);

		fOverlayStore.load();
		fOverlayStore.start();

		createHeader(parent);

		TabFolder folder = new TabFolder(parent, SWT.NONE);
		folder.setLayout(new TabFolderLayout());
		folder.setLayoutData(new GridData(GridData.FILL_BOTH));

		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(PreferencesMessages.getString("CEditorPreferencePage.generalTabTitle")); //$NON-NLS-1$
		item.setControl(createAppearancePage(folder));

		item = new TabItem(folder, SWT.NONE);
		item.setText(PreferencesMessages.getString("CEditorPreferencePage.colorsTabTitle")); //$NON-NLS-1$
		item.setControl(createSyntaxPage(folder));

		item= new TabItem(folder, SWT.NONE);
		item.setText(PreferencesMessages.getString("CEditorPreferencePage.hoverTab.title")); //$NON-NLS-1$
		item.setControl(fCEditorHoverConfigurationBlock.createControl(folder));

		item= new TabItem(folder, SWT.NONE);
		item.setText(PreferencesMessages.getString("CEditorPreferencePage.folding.title")); //$NON-NLS-1$
		item.setControl(fFoldingConfigurationBlock.createControl(folder));

		initialize();

		return folder;
	}

	private void initialize() {

		initializeFields();

		for (int i = 0; i < fAppearanceColorListModel.length; i++) {
			fAppearanceColorList.add(fAppearanceColorListModel[i][0]);
		}
		fAppearanceColorList.getDisplay().asyncExec(new Runnable() {
			public void run() {
				fAppearanceColorList.select(0);
				handleAppearanceColorListSelection();
			}
		});

		for (int i = 0; i < fListModel.length; i++) {
			fList.add(fListModel[i][0]);
		}
		fList.getDisplay().asyncExec(new Runnable() {
			public void run() {
				fList.select(0);
				handleListSelection();
			}
		});

		fFoldingConfigurationBlock.initialize();

	}

	/*
	 * @see PreferencePage#performOk()
	 */
	public boolean performOk() {
		fCEditorHoverConfigurationBlock.performOk();
		fFoldingConfigurationBlock.performOk();
		return super.performOk();
	}

	/*
	 * @see PreferencePage#performDefaults()
	 */
	protected void performDefaults() {

		super.performDefaults();

		handleAppearanceColorListSelection();
		handleListSelection();

		fCEditorHoverConfigurationBlock.performDefaults();
		fFoldingConfigurationBlock.performDefaults();

		fPreviewViewer.invalidateTextPresentation();
	}

	/*
	 * @see DialogPage#dispose()
	 */
	public void dispose() {

		fFoldingConfigurationBlock.dispose();

		if (fCTextTools != null) {
			fCTextTools = null;
		}

		if (fOverlayStore != null) {
			fOverlayStore.stop();
			fOverlayStore = null;
		}

		super.dispose();
	}


	private String loadPreviewContentFromFile(String filename) {
		String line;
		String separator = System.getProperty("line.separator"); //$NON-NLS-1$
		StringBuffer buffer = new StringBuffer(512);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append(separator);
			}
		} catch (IOException io) {
			CUIPlugin.getDefault().log(io);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return buffer.toString();
	}

}
