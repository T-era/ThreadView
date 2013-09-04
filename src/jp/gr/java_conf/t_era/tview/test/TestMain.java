package jp.gr.java_conf.t_era.tview.test;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import jp.gr.java_conf.t_era.tview.Node;
import jp.gr.java_conf.t_era.tview.NodeCreator;
import jp.gr.java_conf.t_era.tview.NodeThread;
import jp.gr.java_conf.t_era.tview.Tag;
import jp.gr.java_conf.t_era.tview.TagCreator;
import jp.gr.java_conf.t_era.tview.viewer.swing.draw.ExpandingSwingThreadViewer;
import jp.gr.java_conf.t_era.tview.viewer.swing.draw.MyMouseListener;
import jp.gr.java_conf.t_era.tview.viewer.swing.draw.FloatSizeSwingThreadViewer;
import jp.gr.java_conf.t_era.tview.viewer.swing.label.JLabelThreadViewer;

public class TestMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeThread<TestNode, TestTag> thread = createThread();
		test3(thread);
//		test2(thread);
	}
	static void test2(NodeThread<TestNode, TestTag> thread) {
		final JFrame frame = new JFrame();
		JLabelThreadViewer<TestNode, TestTag> stv = new JLabelThreadViewer<TestNode, TestTag>(frame.getContentPane());

		thread.show(stv);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	static void test1(NodeThread<TestNode, TestTag> thread) {
		final JFrame frame = new JFrame();
		FloatSizeSwingThreadViewer<TestNode, TestTag> stv = new FloatSizeSwingThreadViewer<TestNode, TestTag>();
		frame.getContentPane().add(stv);
stv.addMyMouseListener(new MyMouseListener<TestNode, TestTag>() {
	@Override
	public void mouseClicked(MyMouseEvent<TestNode, TestTag> e) {
		if (e.node != null) {
			JOptionPane.showMessageDialog(frame, e.node);
		}
	}
});
		thread.show(stv);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	static void test3(NodeThread<TestNode, TestTag> thread) {
		final JFrame frame = new JFrame();
		ExpandingSwingThreadViewer<TestNode, TestTag> stv = new ExpandingSwingThreadViewer<TestNode, TestTag>(30, 100);
		JScrollPane sp = new JScrollPane(stv);
		frame.getContentPane().add(sp);
stv.addMyMouseListener(new MyMouseListener<TestNode, TestTag>() {
	@Override
	public void mouseClicked(MyMouseEvent<TestNode, TestTag> e) {
		if (e.node != null) {
			JOptionPane.showMessageDialog(frame, e.node);
		}
	}
});
		thread.show(stv);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	private static NodeThread<TestNode, TestTag> createThread() {
		NodeThread<TestNode, TestTag> thread = new NodeThread<TestNode, TestTag>();
		thread.addNode(new Creator("first"));
		TestNode sec = thread.addNode(new Creator("second"));
		thread.addTag(new Creator("tag for sec"));
		thread.addNode(new Creator("third"));
		thread.addNode(new Creator("fourth"));
		thread.addNodeToNode(new Creator("2-third"), sec);
		thread.addTag(new Creator("tag for Thread2"));
		thread.addNode(new Creator("2-fourth"));
		return thread;
	}

	static class Creator extends NodeCreator<TestNode> implements TagCreator<TestNode, TestTag> {
		private final String label;
		private Creator(String label) {
			this.label = label;
		}

		@Override
		protected TestNode newNode(TestNode parent) {
			return new TestNode(parent, label);
		}
		@Override
		public TestTag newTag(TestNode node) {
			return new TestTag(node, label);
		}
	}

	static class TestNode extends Node<TestNode> {
		private final String label;

		public TestNode(TestNode parent, String label) {
			super(parent);
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}
	static class TestTag extends Tag<TestNode> {
		private final String label;

		public TestTag(TestNode node, String label) {
			super(node);
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}
}
