import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.week2.SaveAlbumFragment
import com.example.week2.SaveSongFragment
import com.example.week2.songFileFragment

class LockerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SaveSongFragment()
            1 -> songFileFragment()
            else -> SaveAlbumFragment()
        }
    }
}