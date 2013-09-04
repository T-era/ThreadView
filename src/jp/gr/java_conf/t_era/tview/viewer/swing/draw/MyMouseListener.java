package jp.gr.java_conf.t_era.tview.viewer.swing.draw;

import java.awt.event.MouseEvent;

import jp.gr.java_conf.t_era.tview.Node;
import jp.gr.java_conf.t_era.tview.Tag;

public interface MyMouseListener<TNode extends Node<TNode>, TTag extends Tag<TNode>> {
	void mouseClicked(MyMouseEvent<TNode, TTag> e);

	public class MyMouseEvent<TNode extends Node<TNode>, TTag extends Tag<TNode>> {
		public final MouseEvent origin;
		public final TNode node;
		public final TTag tag;

		MyMouseEvent(MouseEvent origin, TNode node, TTag tag) {
			this.origin = origin;
			this.node = node;
			this.tag = tag;
		}
	}
}
