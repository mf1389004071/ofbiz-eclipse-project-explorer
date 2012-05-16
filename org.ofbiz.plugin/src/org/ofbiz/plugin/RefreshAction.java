package org.ofbiz.plugin;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.ofbiz.plugin.model.OfbizModelSingleton;
import org.ofbiz.plugin.nature.OfbizNature;
import org.ofbiz.plugin.ofbiz.Project;


public class RefreshAction extends Action {
	private final ExplorerView view;
	public RefreshAction(ExplorerView view) {
		this.view = view;
	}
	public void run() {
		view.getRoot().getProjects().clear();
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : workspaceRoot.getProjects()) {
			try {
				if (project.getNature(OfbizNature.ID) != null) {
					LoadOperation loadOperation = new LoadOperation(project);
					new ProgressMonitorDialog(view.getSite().getShell()).run(true, true, loadOperation);					
				}
				Project ofbizProject = OfbizModelSingleton.get().findProjectByEclipseProjectName(project.getName());
				if (ofbizProject != null) {
					ofbizProject.setRoot(view.getRoot());
				}
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private String path;
}