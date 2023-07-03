package uz.fergana.daholar.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.fergana.daholar.R
import uz.fergana.daholar.databinding.FragmentAboutAdibBinding
import uz.fergana.daholar.library.SmoothBottomBar
import uz.fergana.daholar.models.Adib
import kotlin.math.abs


private const val ARG_PARAM1 = "adib"
private const val ARG_PARAM2 = "param2"

class AboutAdibFragment : Fragment() {
    private var param1: Adib? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Adib?
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentAboutAdibBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutAdibBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.GONE
            firestore = Firebase.firestore

            Glide.with(requireContext())
                .load(param1?.photoUrl)
                .apply(
                    RequestOptions().placeholder(
                        R.drawable.plase_img
                    ).centerCrop()
                )
                .into(image)

            if (param1?.isSave == true) {
                saveIcon.setBackgroundResource(R.drawable.save1)
                save.setImageResource(R.drawable.ic_saqlanganlar)
            } else {
                saveIcon.setBackgroundResource(R.drawable.save3)
                save.setImageResource(R.drawable.ic_saqlangan)
            }

            collapsingToolbar.title = param1?.name
            date.text = "(${param1?.birthYear}-${param1?.diedYear})"
            about.text = param1?.about

            appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) == appBarLayout?.totalScrollRange) {
                    date.visibility = View.INVISIBLE
                    toolbar.setBackgroundColor(Color.WHITE)
                    if (searchConteyner.isVisible) {
                        search.setBackgroundResource(R.drawable.grey_tablayout_background)
                    } else {
                        search.setBackgroundResource(R.drawable.tablayout_background)
                    }
                } else if (abs(verticalOffset) != appBarLayout?.totalScrollRange) {
                    date.visibility = View.VISIBLE
                    toolbar.setBackgroundColor(Color.parseColor("#E5E5E5"))
                    if (searchConteyner.isVisible) {
                        search.setBackgroundResource(R.drawable.tablayout_background)
                    } else {
                        search.setBackgroundResource(R.drawable.grey_tablayout_background)
                    }
                }
            }
            backIcon.setOnClickListener {
                Navigation.findNavController(root).popBackStack()
            }
            saveIcon.setOnClickListener {
                if (param1?.isSave == true) {
                    saveIcon.setBackgroundResource(R.drawable.save1)
                    save.setImageResource(R.drawable.ic_saqlanganlar)
                    param1?.isSave = false
                    val adib = Adib(
                        param1?.uid,
                        param1?.name,
                        param1?.birthYear,
                        param1?.diedYear,
                        param1?.type,
                        param1?.about,
                        false,
                        param1?.photoUrl
                    )
                    firestore.collection("adib").document(adib.uid.toString()).set(adib)
                    saveIcon.setBackgroundResource(R.drawable.save3)
                    save.setImageResource(R.drawable.ic_saqlangan)
                } else {
                    saveIcon.setBackgroundResource(R.drawable.save3)
                    save.setImageResource(R.drawable.ic_saqlangan)
                    param1?.isSave = true
                    val adib = Adib(
                        param1?.uid,
                        param1?.name,
                        param1?.birthYear,
                        param1?.diedYear,
                        param1?.type,
                        param1?.about,
                        true,
                        param1?.photoUrl
                    )
                    firestore.collection("adib").document(adib.uid.toString()).set(adib)
                    saveIcon.setBackgroundResource(R.drawable.save1)
                    save.setImageResource(R.drawable.ic_saqlanganlar)
                }
            }
            searchIcon.setOnClickListener {
                backIcon.visibility = View.GONE
                saveIcon.visibility = View.GONE
                searchIcon.visibility = View.GONE
                searchConteyner.visibility = View.VISIBLE
            }
            clearText.setOnClickListener {
                backIcon.visibility = View.VISIBLE
                saveIcon.visibility = View.VISIBLE
                searchIcon.visibility = View.VISIBLE
                searchConteyner.visibility = View.GONE
                search.setText("")
            }

            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    about.text = param1?.about.toString()
                    about.setTextToHighlight(p0.toString())
                    about.setTextHighlightColor("#00B238")
                    about.setCaseInsensitive(true)
                    about.highlight()
                }
            })

        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Adib, param2: String) =
            AboutAdibFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}