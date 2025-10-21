package com.example.mocom_uts

import android.os.Bundle
import android.os.Parcelable
import android.os.Parcel
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mocom_uts.ui.theme.Mocom_utsTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mocom_utsTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}


data class Contact(
    val name: String,
    val address: String,
    val phone: String,
    val email: String
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeString(email)
    }


    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}

enum class AppScreen(val title: String) {
    CONTACTLIST("Contact list"),
    ADDorEDITCONTACT("add/edit contact")
}


@Composable
fun MyApp(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            SuperAppMainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAppMainScreen(modifier: Modifier = Modifier) {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.CONTACTLIST) }


    var contacts by rememberSaveable { mutableStateOf(emptyList<Contact>()) }


    val onAddContact: (Contact) -> Unit = { newContact ->
        contacts = contacts + newContact
    }


    val onContactAdded: () -> Unit = {
        currentScreen = AppScreen.CONTACTLIST
    }


    Scaffold(
        modifier = modifier,
        bottomBar = {
            AppBottomNavigation(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it }
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {

                AppScreen.CONTACTLIST -> Greetings(
                    contacts = contacts,
                    modifier = Modifier.fillMaxSize()
                )


                AppScreen.ADDorEDITCONTACT -> TextEditorScreen(
                    onAddContact = onAddContact,
                    onContactAdded = onContactAdded,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigation(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val screens = AppScreen.values()

    NavigationBar(modifier = modifier) {
        screens.forEach { screen ->
            val icon = when (screen) {

                AppScreen.CONTACTLIST -> Icons.Filled.List
                AppScreen.ADDorEDITCONTACT -> Icons.Filled.Add
            }
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = (currentScreen == screen),
                onClick = { onScreenSelected(screen) }
            )
        }
    }
}


@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Welcome to the Super App!")
        ElevatedButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditorScreen(
    onAddContact: (Contact) -> Unit,
    onContactAdded: () -> Unit,
    modifier: Modifier = Modifier
) {


    var name by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }




    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Add Contact",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {


            ElevatedButton(onClick = {

                val newContact = Contact(
                    name = name,
                    address = address,
                    phone = phone,
                    email = email
                )

                onAddContact(newContact)


                onContactAdded()

            }) {
                Text(
                    "Add",
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Address") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("phone") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") }
        )
    }
}


@Composable
private fun Greetings(
    contacts: List<Contact>,
    modifier: Modifier = Modifier
) {


    if (contacts.isEmpty()) {

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("belum ada kontak")
            Text("gunakan 'add/edit contact' untuk menambahkan list")
        }
    } else {

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(contacts) { contact ->

                ContactItem(
                    contact = contact,
                    onClick = {

                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}



@Composable
private fun ContactItem(
    contact: Contact,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = contact.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))


            Text(text = "Address: ${contact.address}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Phone: ${contact.phone}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Email: ${contact.email}")
        }
    }
}


@Composable
private fun ScientificRow(
    buttonTexts: List<String>,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        buttonTexts.forEach { text ->
            ElevatedButton(
                shape = CircleShape,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFFADD8E6),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 2.dp, vertical = 5.dp),
                onClick = { onButtonClick(text) }
            ) {
                Text(text, fontSize = 12.sp)
            }
        }
    }
}




@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    Mocom_utsTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    Mocom_utsTheme {

        val sampleContacts = listOf(
            Contact("Danar", "solo", "534199", "danarpryio@"),
            Contact("basm", "malang", "344343", "basmalah@")
        )
        Greetings(contacts = sampleContacts)
    }
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingEmptyPreview() {
    Mocom_utsTheme {

        Greetings(contacts = emptyList())
    }
}

@Preview
@Composable
fun MyAppPreview() {
    Mocom_utsTheme {
        MyApp(Modifier.fillMaxSize())
    }
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun TextEditorPreview() {
    Mocom_utsTheme {

        TextEditorScreen(onAddContact = {}, onContactAdded = {})
    }
}


@Preview(showBackground = true)
@Composable
fun SuperAppMainPreview() {
    Mocom_utsTheme {
        SuperAppMainScreen()
    }
}