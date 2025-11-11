import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.keder.flo.R
import com.keder.flo.SaveAlbumRVAdapter
import com.keder.flo.Song
import com.keder.flo.databinding.FragmentSaveAlbumBinding

class SaveAlbumFragment : Fragment() {
    lateinit var binding : FragmentSaveAlbumBinding
    //private var songData = ArrayList<Song>()
    private val database = Firebase.database.reference
    private val savedSongList = ArrayList<Song>()
    private lateinit var albumRVAdapter: SaveAlbumRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveAlbumBinding.inflate(inflater, container, false)


        val albumRVAdapter = SaveAlbumRVAdapter(savedSongList, true)
        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        albumRVAdapter.setMyItemClickListener(object : SaveAlbumRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: String) {
                database.child("songs").child(songId).child("isLike").setValue(false)
                    .addOnSuccessListener {
                        loadSavedSongsFromFirebase()
                        Log.d("Firebase", "$songId 'like' removed")
                    }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadSavedSongsFromFirebase()
    }

    private fun loadSavedSongsFromFirebase(){
        database.child("songs").get().addOnSuccessListener { dataSnapshot ->
            savedSongList.clear()
            for(snapshot in dataSnapshot.children){
                val song = snapshot.getValue(Song::class.java)
                if(song != null && song.isLike){
                    savedSongList.add(song)
                }
            }
            albumRVAdapter.notifyDataSetChanged()
            Log.d("Firebase", "Loaded ${savedSongList.size} liked songs")
        }.addOnFailureListener { Log.e("Firebase", "Failed to load liked songs", it) }
    }

}