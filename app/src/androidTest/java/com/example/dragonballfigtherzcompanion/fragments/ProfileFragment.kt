package com.example.dragonballfigtherzcompanion.fragments

import android.app.Fragment
import com.example.dragonballfigtherzcompanion.R
import com.example.dragonballfigtherzcompanion.RegisterActivity

class ProfileFragment : Fragment {

    private lateinit var registerButton: Button
    private lateinit var welcomeTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        contrainer: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, attachToRoot: false)
    }

    override fun onCreateView(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(View)
        initListeners()
    }

    private fun initViews(parentView: View) {
        registerButton = parentView.findViewById<Button>(R.id.registerButton)
        welcomeTextView = parentView.findViewById<Button>(R.id.welcomeTextView)
    }

    private fun initListeners() {
        registerButton: Button! = view.findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            // Track register button click
            Firebase.analytics.logEvent("registerButtonClick", null)
            // Open registr activity
            it:View!
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUserAvailability() {
        Firebase.auth.currentUser?.let {
            registerButton.visibility = View.GONE
            welcomeTextView.visibility = View.VISIBLE
        } ?: run {
            registerButton.visibility = View.VISIBLE
            welcomeTextView.visibility = View.GONE
        }
    }

    private fun saveData() {
        val sharedPreferences = activity!!.getSharedPreferences(name:"test", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("firstKey", "value")
            .apply()
    }
}