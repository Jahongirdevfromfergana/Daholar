package uz.fergana.daholar.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.fergana.daholar.R
import uz.fergana.daholar.adapters.AdibAdapter
import uz.fergana.daholar.databinding.FragmentSaqlanganlarBinding
import uz.fergana.daholar.library.SmoothBottomBar
import uz.fergana.daholar.models.Adib
import kotlin.math.abs

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SaqlanganlarFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentSaqlanganlarBinding
    private lateinit var list: ArrayList<Adib>
    private lateinit var adibAdapter: AdibAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaqlanganlarBinding.inflate(inflater, container, false)
        binding.apply {

            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.VISIBLE
            firestore = Firebase.firestore

            appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) == appBarLayout?.totalScrollRange) {
                    toolbar.setBackgroundColor(Color.WHITE)
                    saqlanganInfo.textSize = 20f
                } else if (abs(verticalOffset) != appBarLayout?.totalScrollRange) {
                    toolbar.setBackgroundColor(Color.parseColor("#E5E5E5"))
                    saqlanganInfo.textSize = 25f
                }
            }

            searchIcon.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.searchFragment)
            }
            list = ArrayList()
            loadData()
            adibAdapter =
                AdibAdapter(requireContext(), list, object : AdibAdapter.OnItemClickListener {
                    override fun onItemClickListener(adib: Adib, position: Int) {
                        val bundle = Bundle()
                        bundle.putSerializable("adib", adib)
                        Navigation.findNavController(root).navigate(R.id.aboutAdibFragment, bundle)
                    }

                    override fun onItemSaveListener(
                        adib: Adib,
                        position: Int,
                        holder: AdibAdapter.Vh
                    ) {
                        if (adib.isSave == true) {
                            holder.itemAdibBinding.save.setBackgroundResource(R.drawable.save2)
                            holder.itemAdibBinding.saveImg.setImageResource(R.drawable.ic_saqlangan)
                            adib.isSave = false
                            firestore.collection("adib").document(adib.uid.toString()).set(adib)
                            list.remove(adib)
                            adibAdapter.notifyDataSetChanged()
                            if (list.isNotEmpty()) {
                                binding.lottie.visibility = View.INVISIBLE
                            } else {
                                binding.lottie.visibility = View.VISIBLE
                            }
                        } else {
                            holder.itemAdibBinding.save.setBackgroundResource(R.drawable.save1)
                            holder.itemAdibBinding.saveImg.setImageResource(R.drawable.ic_saqlanganlar)
                            adib.isSave = true
                            firestore.collection("adib").document(adib.uid.toString()).set(adib)
                        }
                    }
                })
            rv.adapter = adibAdapter
        }
        return binding.root
    }

    private fun loadData() {
        firestore.collection("adib")
            .get()
            .addOnSuccessListener { querySnapshot ->
                list.clear()
                querySnapshot.forEach { queryDocumentSnapshot ->
                    val adib = queryDocumentSnapshot.toObject(Adib::class.java)
                    adib.uid = queryDocumentSnapshot.id
                    if (adib.isSave == true) {
                        list.add(adib)
                        if (list.isNotEmpty()) {
                            binding.lottie.visibility = View.INVISIBLE
                        } else {
                            binding.lottie.visibility = View.VISIBLE
                        }
                        adibAdapter.notifyDataSetChanged()
                    } else {
                        if (list.isNotEmpty()) {
                            binding.lottie.visibility = View.INVISIBLE
                        } else {
                            binding.lottie.visibility = View.VISIBLE
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaqlanganlarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}