package hansung.ac.kr.androidprogrammingproject.ui.profile;

import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;

public class ProfileViewModel extends ViewModel {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private MutableLiveData<UserAccount> currentAccount;

    public ProfileViewModel() {
        currentAccount = new MutableLiveData<>();
        loadRoomListFromFirebase();
    }

    public LiveData<UserAccount> getCurrentAccount() { return currentAccount; }
    public void loadRoomListFromFirebase() {
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("UserAccount").child(LoginActivity.u_id);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                currentAccount.setValue(userAccount);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}