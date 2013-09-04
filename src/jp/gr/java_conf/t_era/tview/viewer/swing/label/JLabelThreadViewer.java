package jp.gr.java_conf.t_era.tview.viewer.swing.label;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import jp.gr.java_conf.t_era.tview.Node;
import jp.gr.java_conf.t_era.tview.Tag;
import jp.gr.java_conf.t_era.tview.viewer.ThreadViewer;

public class JLabelThreadViewer<TNode extends Node<TNode>, TTag extends Tag<TNode>> implements ThreadViewer<TNode, TTag> {
	private final Container con;
	public JLabelThreadViewer(Container con) {
		this.con = con;
	}

	@Override
	public void Show(final List<TTag> tags, TNode root, final Map<TNode, Date> timeline) {
		con.removeAll();

		final int width = root.getThreadCount();

		con.setLayout(new GridLayout(0, width));
		class NodeAppender {
			List<NodeInTimeline> list = new ArrayList<NodeInTimeline>();

			void showNode(Node<TNode> node, int ix, int iy, int indexPrevious) {
				int index = ix + iy * width;

				TTag tag = null;
				for (TTag temp : tags) {
					if (temp.getNode().equals(node)) {
						tag = temp;
					}
				}
				for (int i = indexPrevious; i < index - 1; i ++) {
					con.add(new JLabel(""));
				}
				JLabel label = new JLabel(node.toString());
				if (tag != null) {
					label.setToolTipText(tag.toString());
				}
				con.add(label);

				int dx = 0;
				for (Node<TNode> child : node.getChildren()) {
					list.add(new NodeInTimeline(timeline.get(child), child, ix + dx));
					dx += child.getThreadCount();
				}

				if (! list.isEmpty()) {
					Collections.sort(list);
					NodeInTimeline first = list.remove(0);
					showNode(first.node, first.x, iy + 1, index);
				}
			}

			class NodeInTimeline implements Comparable<NodeInTimeline> {
				final Date time;
				final Node<TNode> node;
				final int x;
				NodeInTimeline(Date time, Node<TNode> node, int x) {
					this.time = time;
					this.node = node;
					this.x = x;
				}
				@Override
				public int compareTo(NodeInTimeline o) {
					return time.compareTo(o.time);
				}

			}
		}

		new NodeAppender().showNode(root, 0, 0, 0);

	}
}
