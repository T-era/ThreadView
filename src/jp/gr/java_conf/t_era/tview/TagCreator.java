package jp.gr.java_conf.t_era.tview;

public interface TagCreator<TNode extends Node<TNode>, TTag extends Tag<TNode>> {
	TTag newTag(TNode node);
}
