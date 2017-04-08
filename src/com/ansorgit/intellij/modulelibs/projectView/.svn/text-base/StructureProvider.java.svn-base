package com.ansorgit.intellij.modulelibs.projectView;

import com.ansorgit.intellij.modulelibs.projectView.nodes.AllExternalLibsNode;
import com.ansorgit.intellij.modulelibs.projectView.nodes.ExternalModuleLibrariesNode;
import com.ansorgit.intellij.modulelibs.projectView.nodes.ModuleLibrariesNode;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.ExternalLibrariesNode;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

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
 * <p/>
 * User: jansorg
 * Date: 27.10.10
 * Time: 10:42
 */
public class StructureProvider implements TreeStructureProvider {
    public Collection<AbstractTreeNode> modify(AbstractTreeNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {
        if (parent instanceof ExternalLibrariesNode) {
            return modifyExternalLibrariesNode((ExternalLibrariesNode) parent, children, settings);
        } else if (parent instanceof PsiDirectoryNode && dirIsModuleRoot((PsiDirectoryNode) parent)) {
            return modifyModuleRootNode(findModule((PsiDirectoryNode) parent), children, settings);
        }

        return children;
    }

    private Module findModule(PsiDirectoryNode parent) {
        VirtualFile dir = parent.getVirtualFile();

        if (dir != null) {
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(parent.getProject()).getFileIndex();
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

        if (dir != null) {
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(directoryNode.getProject()).getFileIndex();
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
        addModuleSubgroups(parent, settings, result);

        return result;
    }

    private static void addModuleSubgroups(ExternalLibrariesNode parent, ViewSettings settings, List<AbstractTreeNode> result) {
        Module[] modules = ModuleManager.getInstance(parent.getProject()).getModules();
        for (Module module : modules) {
            ExternalModuleLibrariesNode node = new ExternalModuleLibrariesNode(module, settings, parent.getProject());
            if (!node.getChildren().isEmpty()) {
                result.add(node);
            }
        }
    }

    private AbstractTreeNode createAllLibrariesNode(ExternalLibrariesNode parent, Collection<AbstractTreeNode> children, ViewSettings settings) {
        return new AllExternalLibsNode(parent.getProject(), settings, children);
    }

    public Object getData(Collection<AbstractTreeNode> selected, String dataName) {
        return null;
    }
}
