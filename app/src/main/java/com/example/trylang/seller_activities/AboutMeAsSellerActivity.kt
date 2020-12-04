package com.example.trylang.seller_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.trylang.R
import com.example.trylang.handlers.UserSellerInfoHandler
import com.example.trylang.handlers.UserSkillsHandler
import com.example.trylang.models.Service
import com.example.trylang.models.User
import com.example.trylang.models.UserSellerInfo
import com.example.trylang.models.UserSkills
import com.example.trylang.sellerfragments.serviceGettingEdited
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class AboutMeAsSellerActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var toolBar: Toolbar
    lateinit var descriptionEditText: EditText
    lateinit var addNewSkillsTextView: TextView
    lateinit var previousSchoolEditText: EditText
    lateinit var skillsListView: ListView
    lateinit var addSkillButton: ImageButton
    lateinit var cancelButton: ImageButton
    lateinit var addSkillEditText: EditText
    lateinit var expertiseSpinner : Spinner
    lateinit var educationStatusSpinner: Spinner
    lateinit var skillsContainer: ConstraintLayout
    lateinit var mainButton: Button
    var userSellerInfoHandler = UserSellerInfoHandler()
    var userSellerInfoArrayList : ArrayList<UserSellerInfo> = ArrayList()
    var userSkillsArrayList: ArrayList<UserSkills> = ArrayList()
    lateinit var userSkillsArrayAddapter: ArrayAdapter<UserSkills>
    var userSkillsHandler = UserSkillsHandler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me_as_seller)
        //Map everything
        toolBar = findViewById(R.id.toolBar_activityAboutMeAsSeller)
        descriptionEditText = findViewById(R.id.description_activityAboutMeAsSeller)
        addNewSkillsTextView = findViewById(R.id.addNewSkills_activityAboutMeAsSeller)
        previousSchoolEditText = findViewById(R.id.schoolEditText_activityAboutMeAsSeller)
        skillsListView = findViewById(R.id.skillsListView_activityAboutMeAsSeller)
        skillsListView.isEnabled = false
        addSkillButton = findViewById(R.id.addImageButton_activityAboutMeAsSeller)
        cancelButton = findViewById(R.id.cancleImageButton_activityAboutMeAsSeller)
        addSkillEditText = findViewById(R.id.addSkillsEditText_acitivityAboutMeAsSeller)
        expertiseSpinner = findViewById(R.id.expertiseLevelSpinner_acitivityAboutMeAsSeller)
        educationStatusSpinner = findViewById(R.id.educationStatus_activityAboutMeAsSeller)
        skillsContainer = findViewById(R.id.skillsContainer)
        mainButton = findViewById(R.id.mainButton_activityAboutMeAsSeller)

        fetchSellerInfo()

        //Add the spinners
        //Skill level
        ArrayAdapter.createFromResource(this, R.array.skillLevel, android.R.layout.simple_spinner_item)
            .also {
                    adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                expertiseSpinner.adapter = adapter
            }
        expertiseSpinner.onItemSelectedListener = this
        //Education Status
        ArrayAdapter.createFromResource(this, R.array.educationStatus, android.R.layout.simple_spinner_item)
            .also {
                    adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                educationStatusSpinner.adapter = adapter
            }
        educationStatusSpinner.onItemSelectedListener = this

        //List view for the skills





        //Toolbar
        toolBar.setTitle("About me as a Seller")
        toolBar.setNavigationOnClickListener {
            finish()
        }
        //add new skills clickable text view
        addNewSkillsTextView.setOnClickListener {
            skillsContainer.isGone = false
            addNewSkillsTextView.isVisible = false
        }

        cancelButton.setOnClickListener{
            skillsContainer.isGone = true
            addNewSkillsTextView.isVisible = true
            addSkillEditText.text.clear()
        }





        addSkillButton.setOnClickListener{
            if (addSkillEditText.text.toString().isEmpty()){
                addSkillEditText.error = "Enter skill before adding."
                addSkillEditText.requestFocus()
            } else {
                val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
                val skill = UserSkills(
                    userUid = currentUserUid,
                    skillExpertise = expertiseSpinner.selectedItem.toString(),
                    skill = addSkillEditText.text.toString()
                )
                if (userSkillsHandler.createSkill(skill)) {
                    Toast.makeText(this, "Skill added", Toast.LENGTH_SHORT).show()
                }
                addSkillEditText.text.clear()
                expertiseSpinner.setSelection(0)
            }
        }

        mainButton.setOnClickListener {
            if (mainButton.text.toString() == "Update"){
                previousSchoolEditText.isEnabled = true
                descriptionEditText.isEnabled = true
                addSkillEditText.isEnabled = true
                skillsListView.isEnabled = true
                addNewSkillsTextView.isEnabled = true
                mainButton.text = "Save"
            } else if (mainButton.text.toString() == "Save") {
                previousSchoolEditText.isEnabled = false
                descriptionEditText.isEnabled = false
                addSkillEditText.isEnabled = false
                skillsListView.isEnabled = false
                mainButton.text = "Update"
                addNewSkillsTextView.isEnabled = false

                saveUserToFirebaseDatabase()

            }


        }

        registerForContextMenu(skillsListView)


    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_edit_delete, menu)
        val menuItem = menu!!.findItem(R.id.edit)
        menuItem.setVisible(false)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.delete -> {
                if (userSkillsHandler.deleteSkill(userSkillsArrayList[info.position])) {
                    Toast.makeText(this, "Skill deleted", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        val userSkillsHandler = UserSkillsHandler()
        userSkillsHandler.userSkillsRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
                userSkillsArrayList.clear()
                p0.children.forEach {
                    val userSkills = it.getValue(UserSkills::class.java)
                    if (userSkills!!.userUid == currentUserUid){
                    userSkillsArrayList.add(userSkills!!)}

                }
                userSkillsArrayAddapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, userSkillsArrayList)
                skillsListView.adapter = userSkillsArrayAddapter

            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }


        })

    }

    private fun fetchSellerInfo(){
        userSellerInfoHandler.userSellerInfoRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                userSellerInfoArrayList.clear()
                p0.children.forEach {
                        it -> val userSellerInfo = it.getValue(UserSellerInfo::class.java)
                    userSellerInfoArrayList.add(userSellerInfo!!)
                }
                val userData = userSellerInfoArrayList.find { it.uid == uid }
                descriptionEditText.setText(userData!!.description)
                previousSchoolEditText.setText(userData!!.previousSchool)
                var selection: Int? = 0
                when(userData.educationalAttainment){
                    "Primary School Graduate" -> {selection = 0}
                    "Secondary School Graduate" -> { selection = 1}
                    "High School Graduate" -> {selection = 2 }
                    "University Graduate" -> {selection = 3 }
                    "Postgraduate" -> {selection = 4}
                    "On going" -> {selection = 5}
                }
                educationStatusSpinner.setSelection(selection!!)

            }
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }

    private fun saveUserToFirebaseDatabase() {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val ref = FirebaseDatabase.getInstance().getReference("/user_seller_info/$uid")
            val userSellerInfo = UserSellerInfo(
                uid = uid,
                description = descriptionEditText.text.toString(),
                educationalAttainment = educationStatusSpinner.selectedItem.toString(),
                previousSchool = previousSchoolEditText.text.toString())
            ref.setValue(userSellerInfo)
        Toast.makeText(this, "Account updated", Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}