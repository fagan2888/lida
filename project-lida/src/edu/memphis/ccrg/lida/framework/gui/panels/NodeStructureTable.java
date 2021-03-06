/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NodeStructureTable.java
 *
 * Created on Jun 6, 2011, 5:15:10 PM
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.gui.utils.GuiUtils;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * A {@link GuiPanel} which displays the attributes of a {@link NodeStructure}
 * in a table.
 * @author Javier Snaider
 */
public class NodeStructureTable extends GuiPanelImpl {

    private static final Logger logger = Logger.getLogger(NodeStructureTable.class.getCanonicalName());
    private NodeStructure nodeStructure;
    private FrameworkModule module;
    private NodeStructureTableModel nodeStructureTableModel;

    /** Creates new form NodeStructureTable */
    public NodeStructureTable() {
        nodeStructureTableModel=new NodeStructureTableModel();
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        refreshButton = new javax.swing.JButton();
        nodeStructurePane = new javax.swing.JScrollPane();
        nodeStructureTable = new javax.swing.JTable();

        jToolBar1.setRollover(true);

        refreshButton.setText("Refresh");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshButton);

        nodeStructureTable.setModel(nodeStructureTableModel);
        nodeStructureTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        nodeStructureTable.setMaximumSize(new java.awt.Dimension(1000, 1000));
        nodeStructurePane.setViewportView(nodeStructureTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(nodeStructurePane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(275, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addComponent(nodeStructurePane, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        refresh();
}//GEN-LAST:event_refreshButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JScrollPane nodeStructurePane;
    private javax.swing.JTable nodeStructureTable;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Definition of this Panel should include a parameter for the ModuleName for the
     * module from which the NodeStructure will be obtained.
     * E.g., workspace.PerceptualBuffer or PerceptualAssociativeMemory
     * @see edu.memphis.ccrg.lida.framework.gui.panels.GuiPanelImpl#initPanel(java.lang.String[])
     */
    @Override
    public void initPanel(String[] param) {
        if (param == null || param.length == 0) {
            logger.log(Level.WARNING,
                    "Error initializing NodeStructureTable, not enough parameters.",
                    0L);
            return;
        }
        module = GuiUtils.parseFrameworkModule(param[0], agent);

        if (module != null) {
            display(module.getModuleContent());
        } else {
            logger.log(Level.WARNING,
                    "Unable to parse module {1}. Panel not initialized.",
                    new Object[]{0L, param[0]});
        }
    }

    @Override
    public void refresh() {
        display(module.getModuleContent());
    }

    /*
     * Implementation of abstract table model to adapt a NodeStructure to a Table.
     * Columns are the attributes of the Nodes in the NodeStructure.  Rows are the Nodes
     * @author Javier Snaider
     */
    private class NodeStructureTableModel extends AbstractTableModel {
        //TODO support links as well

        private String[] columnNames = {"Node", "ID", "Current Activation", "Base-Level Activation", "Threshold"};
        private int[] columnAlign = {SwingConstants.LEFT,SwingConstants.RIGHT,SwingConstants.RIGHT,SwingConstants.RIGHT,SwingConstants.RIGHT};
        private DecimalFormat df = new DecimalFormat("0.0000");
        private Map<String,Integer>columnAlignmentMap = new HashMap<String,Integer>();

        public NodeStructureTableModel(){
            for (int i=0;i<columnNames.length;i++){
                columnAlignmentMap.put(columnNames[i], columnAlign[i]);
            }
        }
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return nodeStructure.getNodeCount();
        }

        @Override
        public String getColumnName(int column) {
            if (column < columnNames.length) {
                return columnNames[column];
            }
            return "";
        }

        /**
         * Depending on the columnIndex, the appropriate method is called
         * to get an attribute of the Node.
         *
         * @param rowIndex which row that is being filled in
         * @param columnIndex the attribute being asked for
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Node node = null;
            if (rowIndex > nodeStructure.getNodeCount() || columnIndex > columnNames.length
                    || rowIndex < 0 || columnIndex < 0) {
                return null;
            }
            Collection<Node> nodes = nodeStructure.getNodes();
            Iterator<Node> it = nodes.iterator();
            //TODO optimize
            for (int i = 0; i <= rowIndex; i++) {
                node = it.next();
            }
            switch (columnIndex) {
                case 0:
                	if(node == null){
                    	return "";
                    }else{
                    	return node.getLabel();
                    }                    
                case 1:
                	if(node == null){
                    	return "";
                    }else{
                    	return node.getId();
                    } 
                case 2:
                	if(node == null){
                    	return "";
                    }else{
                    	return df.format(node.getActivation());
                    } 
                case 3:
                    if (node instanceof PamNode) {
                        return df.format(((PamNode) node).getBaseLevelActivation());
                    } else {
                        return "";
                    }
                case 4:
                    if (node instanceof PamNode) {
                        return df.format(PerceptualAssociativeMemoryImpl.getPerceptThreshold());
                    } else {
                        return "";
                    }
                default:
                    return "";
            }
        }

        /**
         * @return the columnAlignmentMap
         */
        public Map<String, Integer> getColumnAlignmentMap() {
            return columnAlignmentMap;
        }
    }

    @Override
    public void display(Object o) {
        if (o instanceof NodeStructure) {
            nodeStructure = (NodeStructure) o;
            ((AbstractTableModel) nodeStructureTable.getModel()).fireTableStructureChanged();
        } else {
            logger.log(Level.WARNING, "Can only display NodeStructure, but received {1} from module {2}",
                    new Object[]{TaskManager.getCurrentTick(), o, module});
        }
    }

    @SuppressWarnings("unused")
	private class AlignedColumnTableModel extends DefaultTableColumnModel {

        private DefaultTableCellRenderer render;
        public AlignedColumnTableModel(){
            render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        @Override
        public void addColumn(TableColumn column) {
            if(nodeStructureTableModel.getColumnAlignmentMap().get(column.getHeaderValue().toString())==SwingConstants.RIGHT){
                column.setCellRenderer(render);
            }
            super.addColumn(column);
        }
    }
}
