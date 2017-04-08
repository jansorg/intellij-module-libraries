package com.ansorgit.intellij.modulelibs.projectView.nodes;

import com.ansorgit.intellij.modulelibs.ProjectNodeUtil;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * A node which groups the libraries of a single module.
 *
 * At first it was supposed to only show the external library dependencies but currently it shows all libraries
 * (i.e. the same as ModuleLibrariesNode).
 *
 * User: jansorg
 * Date: 27.10.10
 * Time: 10:57
 */
public class ExternalModuleLibrariesNode extends ProjectViewNode<Module> {
    /**
     * Creates an instance of the project view node.
     *
     * @param project  the project containing the node.
     * @param module   the object (for example, a PSI element) represented by the project view node
     * @param settings the settings of the project view.
     */
    public ExternalModuleLibrariesNode(Module module, ViewSettings settings, Project project) {
        super(project, module, settings);
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        return false;
    }

    @NotNull
    @Override
    public Collection<? extends AbstractTreeNode> getChildren() {
        return ProjectNodeUtil.createModuleLibraries(getValue(), getSettings(), false);
    }

    @Override
    protected void update(PresentationData presentation) {
        Module module = getValue();

        presentation.setPresentableText(module.getName() + " (internal & external)");
        //presentation.setOpenIcon(module.getModuleType().getNodeIcon(true));
        //presentation.setClosedIcon(module.getModuleType().getNodeIcon(false));
    }

    /**
     * Overridden to avoid invalid tree navigation. The jetbrains code expects a node value to only appear once in the
     * whole tree.
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        return this == object;
    }

    @Override
    public boolean canRepresent(Object element) {
        return this == element;
    }
}
