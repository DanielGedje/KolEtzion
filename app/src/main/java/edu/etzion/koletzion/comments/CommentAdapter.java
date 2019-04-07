package edu.etzion.koletzion.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.etzion.koletzion.R;
import edu.etzion.koletzion.database.BitmapSerializer;
import edu.etzion.koletzion.models.BroadcastPost;
import edu.etzion.koletzion.models.Comment;
import edu.etzion.koletzion.models.Profile;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
	List<Comment> comments;
	
	public CommentAdapter(BroadcastPost post) {
		comments = post.getComments();
	}
	
	@NonNull
	@Override
	public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new CommentViewHolder(
				LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent,
						false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
		Profile p = comments.get(position).getProfile();
		holder.tvCommentName.setText(p.getFirstName() + " " + p.getLastName());
		holder.tvCommentContent.setText(comments.get(position).getContent());
		holder.ivCommenter.setImageBitmap(BitmapSerializer.decodeStringToBitmap(
				comments.get(position).getProfile().getEncodedBitMapImage()
		));
	}
	
	@Override
	public int getItemCount() {
		return comments.size();
	}
	
	class CommentViewHolder extends RecyclerView.ViewHolder {
		ImageView ivCommenter;
		TextView tvCommentName;
		TextView tvCommentContent;
		
		public CommentViewHolder(@NonNull View itemView) {
			super(itemView);
			this.ivCommenter = itemView.findViewById(R.id.ivCommenter);
			this.tvCommentName = itemView.findViewById(R.id.tvCommentName);
			this.tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
		}
	}
}
