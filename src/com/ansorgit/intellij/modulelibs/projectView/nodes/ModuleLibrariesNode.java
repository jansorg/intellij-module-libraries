package com.ansorgit.intellij.modulelibs.projectView.nodes;

import com.ansorgit.intellij.modulelibs.ProjectNodeUtil;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * A module libraries node contains a module's dependencies as children and is displayed as a child node
 * of a module node.
 *
 * @author jansorg
 */
public class ModuleLibrariesNode extends ProjectViewNode<String> {
    private Module module;

    /**
     * Creates an instance of the project view node.
     *
     * @param viewSettings the settings of the project view.
     */
    public ModuleLibrariesNode(Module module, ViewSettings viewSettings) {
        super(module.getProject(), UUID.randomUUID().toString(), viewSettings);
        this.module = module;
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        return someChildContainsFile(file, false);
    }

    @NotNull
    @Override
    public Collection<? extends AbstractTreeNode> getChildren() {
        return ProjectNodeUtil.createModuleLibraries(module, getSettings(), false);
    }

    @Override
    protected void update(PresentationData presentation) {
        presentation.setPresentableText("Libraries");
        presentation.setIcon(PlatformIcons.LIBRARY_ICON);
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }

    @Override
    public boolean canRepresent(Object element) {
        return false;
    }
}
