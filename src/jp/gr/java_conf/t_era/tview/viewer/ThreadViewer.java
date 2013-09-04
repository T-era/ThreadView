package jp.gr.java_conf.t_era.tview.viewer;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.t_era.tview.Node;
import jp.gr.java_conf.t_era.tview.Tag;

public interface ThreadViewer<TNode extends Node<TNode>, TTag extends Tag<TNode>> {
	void Show(List<TTag> tags, TNode root, Map<TNode, Date> timeline);
}
