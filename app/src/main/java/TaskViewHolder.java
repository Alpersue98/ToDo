import android.widget.TextView;

public class TaskViewHolder {
    public final TextView nameTextView;
    public ItemViewHolder(ItemBinding binding) {
        super(binding.getRoot());
        nameTextView = binding.nameTextView;
    }
}
