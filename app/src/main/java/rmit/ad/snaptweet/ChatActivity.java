package rmit.ad.snaptweet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

import rmit.ad.snaptweet.Adapter.ChatRecyclerAdapter;
import rmit.ad.snaptweet.Model.ChatMessageModel;
import rmit.ad.snaptweet.Model.ChatroomModel;
import rmit.ad.snaptweet.Model.UserModel;


public class ChatActivity extends AppCompatActivity {
    UserModel otherUser;
    String chatroomID;
    ChatroomModel chatroomModel;
    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recycleView;
    ChatRecyclerAdapter adapter;
    protected void  onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chat);
        otherUser=AndroidUtil.getUserFromIntent(getIntent());
        chatroomID=FirebaseUtil.getChatroomID(FirebaseUtil.currentUserId(),otherUser.getUserId());

        messageInput=findViewById(R.id.chat_message_input);
        sendMessageBtn=findViewById(R.id.message_send_btn);
        backBtn=findViewById(R.id.back_btn);
        otherUsername=findViewById(R.id.other_username);
        recycleView=findViewById(R.id.chat_recycler_view);

        backBtn.setOnClickListener((v) ->{
            onBackPressed();
        });
        otherUsername.setText(otherUser.getUsername());
        sendMessageBtn.setOnClickListener((v -> {
           String message = messageInput.getText().toString().trim();
           if(message.isEmpty())
               return;
           sendMessageToUser(message);
        }));
        getOrCreateChatroomModel();
        setupChatRecycleView();

    }
void setupChatRecycleView(){
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomID)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recycleView.setLayoutManager(manager);
        recycleView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recycleView.smoothScrollToPosition(0);
            }
        });

    }
    void sendMessageToUser(String message){
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomID).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomID).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");
                            sendNotification(message);
                        }
                    }
                });

    }
    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(chatroomID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if(chatroomModel==null){
                    //first time chat
                    chatroomModel = new ChatroomModel(
                            chatroomID,
                            Arrays.asList(FirebaseUtil.currentUserId(),otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomID).set(chatroomModel);
                }
            }
        });
    }

    void setupChatRecyclerView(){
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomID)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        com.firebase.ui.firestore.FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recycleView.setLayoutManager(manager);
        recycleView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recycleView.smoothScrollToPosition(0);
            }
        });
    }
}
