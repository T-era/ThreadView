package jp.gr.java_conf.t_era.tview.viewer.swing.draw;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.t_era.tview.Node;
import jp.gr.java_conf.t_era.tview.Tag;
import jp.gr.java_conf.t_era.tview.viewer.ThreadViewer;
import jp.gr.java_conf.t_era.tview.viewer.swing.draw.MyMouseListener.MyMouseEvent;

public class FloatSizeSwingThreadViewer<TNode extends Node<TNode>, TTag extends Tag<TNode>> extends Container implements ThreadViewer<TNode, TTag> {
	private static final long serialVersionUID = 3520151717710564554L;
	private final List<NodeView> allElement = new ArrayList<NodeView>();
	private final List<MyMouseListener<TNode, TTag>> listeners = new ArrayList<MyMouseListener<TNode, TTag>>();

	public FloatSizeSwingThreadViewer() {
		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (MyMouseListener<TNode, TTag> listener : listeners) {
					listener.mouseClicked(new MyMouseEvent<TNode, TTag>(e, getNodeHere(e.getX(), e.getY()), getTagHere(e.getX(), e.getY())));
				}
			}
		});
	}
	private TNode getNodeHere(int x, int y) {
		NodeView nv = getNodeView(x, y);
		return nv == null ? null : nv.node;
	}
	private TTag getTagHere(int x, int y) {
		NodeView nv = getNodeView(x, y);
		return nv == null ? null : nv.tag;
	}
	private NodeView getNodeView(int x, int y) {
		int allCount = allElement.size();
		int index = (allCount + 1) * y / getSize().height;

		if (index < 0 || index >= allCount) {
			return null;
		}
		return allElement.get(index);
	}
	public void addMyMouseListener(MyMouseListener<TNode, TTag> listener) {
		listeners.add(listener);
	}

	@Override
	public void paint(Graphics g) {
		super.paintComponents(g);

		for (NodeView view : allElement) {
			view.draw(g, this.getSize());
		}
	}

	@Override
	public void Show(final List<TTag> tags, TNode root, final Map<TNode, Date> timeline) {
		allElement.clear();
		final int width = root.getThreadCount() + 2;
		final int height = timeline.size() + 1;

		class NodeAppender {
			List<NodeInTimeline> list = new ArrayList<NodeInTimeline>();

			void showNode(TNode node, int ix, int iy, NodeView parent) {
				double x = ix / (double)width;
				double y = iy / (double)height;
				TTag tag = null;
				for (TTag temp : tags) {
					if (temp.getNode().equals(node)) {
						tag = temp;
					}
				}
				NodeView nv = new NodeView(node, tag, x, y, parent);
				allElement.add(nv);

				int dx = 0;
				for (TNode child : node.getChildren()) {
					list.add(new NodeInTimeline(timeline.get(child), child, ix + dx, nv));
					dx += child.getThreadCount();
				}

				if (! list.isEmpty()) {
					Collections.sort(list);
					NodeInTimeline first = list.remove(0);
					showNode(first.node, first.x, iy + 1, first.parent);
				}
			}

			class NodeInTimeline implements Comparable<NodeInTimeline> {
				final Date time;
				final TNode node;
				final int x;
				final NodeView parent;
				NodeInTimeline(Date time, TNode node, int x, NodeView parent) {
					this.time = time;
					this.node = node;
					this.x = x;
					this.parent = parent;
				}
				@Override
				public int compareTo(NodeInTimeline o) {
					return time.compareTo(o.time);
				}
			}
		}

		new NodeAppender().showNode(root, 1, 1, null);
	}

	private class NodeView {
		private final double x;
		private final double y;
		private final TNode node;
		private final TTag tag;
		private final NodeView parent;
		private final FloatSizeShowString showString;

		public NodeView(TNode node, TTag tag, double x, double y, NodeView parent) {
			this.node = node;
			this.tag = tag;
			this.x = x;
			this.y = y;
			this.parent = parent;
			this.showString = new FloatSizeShowString(node.toString(), tag == null ? null : tag.toString(), x, y);
		}
		public void draw(Graphics g, Dimension containerSize) {
			if (parent != null) {
				Point t = this.getTail(containerSize);
				Point p = parent.getTail(containerSize);
				g.drawLine(t.x, t.y, p.x, p.y);
			}
			showString.show(g, containerSize);
		}
		private Point getTail(Dimension containerSize) {
			return new Point((int)(containerSize.width * x)
					, (int)(containerSize.height * y));
		}
	}
}
