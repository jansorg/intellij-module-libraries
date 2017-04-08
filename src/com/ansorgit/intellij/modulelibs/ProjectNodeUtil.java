package com.ansorgit.intellij.modulelibs;

import com.ansorgit.intellij.modulelibs.projectView.nodes.FixedPsiDirectoryNode;
import com.ansorgit.intellij.modulelibs.projectView.nodes.NamedLibraryElement;
import com.ansorgit.intellij.modulelibs.projectView.nodes.NamedLibraryElementNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.util.PathUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectNodeUtil {
    public static Collection<? extends AbstractTreeNode> createModuleLibraries(Module module, ViewSettings settings, boolean onlyExternal) {
        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(module.getProject()).getFileIndex();

        List<AbstractTreeNode> children = new ArrayList<AbstractTreeNode>();
        final ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        final OrderEntry[] orderEntries = moduleRootManager.getOrderEntries();

        for (OrderEntry entry : orderEntries) {
            if (entry instanceof LibraryOrderEntry) {
                LibraryOrderEntry libraryOrderEntry = (LibraryOrderEntry) entry;

                final Library library = libraryOrderEntry.getLibrary();
                if (library == null) {
                    continue;
                }

                if (onlyExternal && !hasExternalEntries(fileIndex, libraryOrderEntry)) {
                    continue;
                }

                final String libraryName = library.getName();
                if (libraryName == null || libraryName.length() == 0) {
                    addLibraryChildren(libraryOrderEntry, children, module.getProject(), settings);
                } else {
                    children.add(new NamedLibraryElementNode(module.getProject(), new NamedLibraryElement(module, libraryOrderEntry), settings));
                }
            }
        }

        return children;
    }

    static boolean hasExternalEntries(ProjectFileIndex index, LibraryOrderEntry orderEntry) {
        VirtualFile[] files = orderEntry.getRootFiles(OrderRootType.CLASSES);
        for (VirtualFile file : files) {
            if (!index.isInContent(PathUtil.getLocalFile(file))) return true;
        }

        return false;
    }

    public static void addLibraryChildren(final LibraryOrderEntry entry,
                                          final List<AbstractTreeNode> children,
                                          Project project, ViewSettings settings) {

        final PsiManager psiManager = PsiManager.getInstance(project);
        final VirtualFile[] files = entry.getRootFiles(OrderRootType.CLASSES);

        for (final VirtualFile file : files) {
            final PsiDirectory psiDir = psiManager.findDirectory(file);
            if (psiDir == null) {
                continue;
            }

            //children.add(new FixedPsiFileNode(project,psiManager.findFile(file), settings));
            children.add(new FixedPsiDirectoryNode(project, psiDir, settings));
        }
    }


}
