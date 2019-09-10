package Data;

import androidx.annotation.NonNull;

import com.example.whackamole.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class DataManagement {

    private static DataManagement dataManagement;
    private static FirebaseDatabase database;

    private static FirebaseFirestore firebaseFirestore;

    public DataManagement() {
    }

    public static DataManagement createSingletonDM() {
        if (dataManagement == null) {
            dataManagement = new DataManagement();
            firebaseFirestore = getFireBaseInstance();
            database = FirebaseDatabase.getInstance();
        }
        return dataManagement;
    }

    public static FirebaseFirestore getFireBaseInstance() {
        return FirebaseFirestore.getInstance();
    }


    public void saveData(String collectionName, final String document, Map<String, Object> data) {
        firebaseFirestore.collection(collectionName).document(document).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println(document + " was added successfully!!!!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error via trying to add: " + document + "\n" + e.toString());
                    }
                });
    }

    public void readData(final FireStoreCallback callback, String collectionName, final List<Map<String, Object>> data) {


        firebaseFirestore.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.add(document.getData());
                            }
                            callback.onCallback(data);
                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                    }
                });
    }
}
