package com.ansorgit.intellij.modulelibs.projectView;

import com.ansorgit.intellij.modulelibs.projectView.nodes.AllAggregatedExternalLibsNode;
import com.ansorgit.intellij.modulelibs.projectView.nodes.ExternalModuleLibrariesNode;
import com.ansorgit.intellij.modulelibs.projectView.nodes.ModuleLibrariesNode;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.ExternalLibrariesNode;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This modifies the original structure of the Project view.
 * It takes the "External libraries" node and adds module subnodes which list all of a module's libraries.
 * <p/>
 * It also adds a "Libraries" node under each module content root node. This node is redundant to the module node in
 * the "External libraries" parent node.
 * <p/>
 *
 * @author jansorg
 */
public class StructureProvider implements TreeStructureProvider {
    private static void addExternalModuleSubgroups(ExternalLibrariesNode parent, ViewSettings settings, List<AbstractTreeNode> result) {
        Project project = parent.getProject();
        if (project == null) {
            return;
        }

        for (Module module : ModuleManager.getInstance(project).getModules()) {
            ExternalModuleLibrariesNode node = new ExternalModuleLibrariesNode(module, settings, project);
            if (!node.getChildren().isEmpty()) {
                result.add(node);
            }
        }
    }

    @NotNull
    public Collection<AbstractTreeNode> modify(@NotNull AbstractTreeNode parent, @NotNull Collection<AbstractTreeNode> children, ViewSettings settings) {
        if (parent instanceof ExternalLibrariesNode) {
            return modifyExternalLibrariesNode((ExternalLibrariesNode) parent, children, settings);
        }

        if (parent instanceof PsiDirectoryNode && dirIsModuleRoot((PsiDirectoryNode) parent)) {
            return modifyModuleRootNode(findModule((PsiDirectoryNode) parent), children, settings);
        }

        return children;
    }

    private Module findModule(PsiDirectoryNode parent) {
        VirtualFile dir = parent.getVirtualFile();
        Project project = parent.getProject();

        if (dir != null && project != null) {
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
            VirtualFile moduleContentRoot = fileIndex.getContentRootForFile(dir);

            if (moduleContentRoot == dir || dir.equals(moduleContentRoot)) {
                return fileIndex.getModuleForFile(dir);
            }
        }

        return null;
    }

    private Collection<AbstractTreeNode> modifyModuleRootNode(Module module, Collection<AbstractTreeNode> children, ViewSettings settings) {
        children.add(new ModuleLibrariesNode(module, settings));

        return children;
    }

    private boolean dirIsModuleRoot(PsiDirectoryNode directoryNode) {
        VirtualFile dir = directoryNode.getVirtualFile();
        Project project = directoryNode.getProject();

        if (dir != null && project != null) {
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
            VirtualFile moduleContentRoot = fileIndex.getContentRootForFile(dir);

            return (moduleContentRoot == dir || dir.equals(moduleContentRoot));
        }

        return false;
    }

    private Collection<AbstractTreeNode> modifyExternalLibrariesNode(ExternalLibrariesNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {
        List<AbstractTreeNode> result = new ArrayList<AbstractTreeNode>();

        //add a "all libraries" node which contains all the available, aggregated libs
        //these nodes are the target for any select in project view action
        result.add(createAllLibrariesNode(parent, children, settings));

        //now add a grouping by module, each module node shows its own external libraries
        addExternalModuleSubgroups(parent, settings, result);

        return result;
    }

    private AbstractTreeNode createAllLibrariesNode(ExternalLibrariesNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {
        return new AllAggregatedExternalLibsNode(parent.getProject(), settings, children);
    }

    public Object getData(Collection<AbstractTreeNode> selected, String dataName) {
        return null;
    }
}
