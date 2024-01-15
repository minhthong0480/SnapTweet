package rmit.ad.snaptweet.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rmit.ad.snaptweet.Adapter.UserAdapter;
import rmit.ad.snaptweet.Model.UserModel;
import rmit.ad.snaptweet.R;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> mUsers;

    EditText search_bar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));

        search_bar = view.findViewById(R.id.search_bar);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);
        recyclerView.setAdapter(userAdapter);

        readUsers();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUser(String s){
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel user = snapshot.getValue(UserModel.class);
                    mUsers.add(user);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void searchUser(String s) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Assuming your collection is named "Users" in Firestore
//        CollectionReference usersCollection = db.collection("Users");
//
//        // Convert the search string to lowercase for case-insensitive search
//        String searchQuery = s.toLowerCase();
//
//        // Perform a case-insensitive search on the "username" field
//        Query query = usersCollection
//                .whereGreaterThanOrEqualTo("username", searchQuery)
//                .whereLessThanOrEqualTo("username", searchQuery + "\uf8ff");
//
//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                mUsers.clear();
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    // Convert the document snapshot to a User object
//                    User user = document.toObject(User.class);
//                    mUsers.add(user);
//                }
//                userAdapter.notifyDataSetChanged();
//            } else {
//                Log.e(TAG, "Error getting documents: ", task.getException());
//            }
//        });
//    }


    private void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(search_bar.getText().toString().equals((""))){
                    mUsers.clear();
                    for (DataSnapshot snapshot : datasnapshot.getChildren()){
                        UserModel user = snapshot.getValue(UserModel.class);
                        mUsers.add(user);
                    }

                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}