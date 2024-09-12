package com.example.foodio.views

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.foodio.services.Firebase
import com.example.foodio.ui.theme.FoodIOTheme

data class ProductLink(
    var category: String = "",
    var foodName: String = "",
    var foodDescription: String = "",
    var foodPrice: Float = 0f,
    var foodAmount: Int = 0,
) {
    fun isNotEmpty(): Boolean {
        return category.isNotEmpty() && foodName.isNotEmpty() && foodDescription.isNotEmpty()
    }
}

@Composable
fun AdminTextField(
    value: String,
    onChange: (String) -> Unit,
    label: String = "Text",
    placeholder: String = "Enter your Text",
    icon: ImageVector,
) {
    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

fun checkAdminValues(values: ProductLink): Boolean {
    if (values.isNotEmpty()) {
        Firebase.addProductToStorage(
            category = values.category,
            foodName = values.foodName,
            foodDescription = values.foodDescription,
            foodPrice = values.foodPrice,
            foodAmount = values.foodAmount
        )
        return true
    }
    return false
}

@Composable
fun AdminForm() {
    Surface {
        var productLink by remember { mutableStateOf(ProductLink()) }
        val context = LocalContext.current

        MyBackButton(context)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                "Admin Console, Add new product in Database!",
                fontFamily = FontFamily.Cursive,
                fontSize = TextUnit(10f, TextUnitType.Em),
                textAlign = TextAlign.Center,
                lineHeight = TextUnit(0.8f, TextUnitType.Em)
            )
            Spacer(modifier = Modifier.height(50.dp))
            AdminTextField(
                value = productLink.category,
                onChange = { data -> productLink = productLink.copy(category = data) },
                icon = Icons.Default.Category,
                label = "Category",
                placeholder = "Example: Hamburger"
            )
            AdminTextField(
                value = productLink.foodName,
                onChange = { data -> productLink = productLink.copy(foodName = data) },
                icon = Icons.Default.DriveFileRenameOutline,
                label = "Food Name",
                placeholder = "Example: Hamburger_0"
            )
            AdminTextField(
                value = productLink.foodDescription,
                onChange = { data -> productLink = productLink.copy(foodDescription = data) },
                icon = Icons.Default.Description,
                label = "Food Description",
                placeholder = "Example: Lorem Ipsum Es"
            )
            AdminTextField(
                value = productLink.foodPrice.toString(),
                onChange = { data ->
                    productLink = try {
                        productLink.copy(foodPrice = data.toFloat())
                    } catch (e: NumberFormatException) {
                        productLink.copy(foodPrice = 0f)
                    }
                },
                icon = Icons.Default.PriceChange,
                label = "Food Price",
                placeholder = "Example: 50.0"
            )
            AdminTextField(
                value = productLink.foodAmount.toString(),
                onChange = { data ->
                    productLink = try {
                        productLink.copy(foodAmount = data.toInt())
                    } catch (e: NumberFormatException) {
                        productLink.copy(foodAmount = 0)
                    }
                },
                icon = Icons.Default.Numbers,
                label = "Food Amount",
                placeholder = "Example: 100.0"
            )
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = {
                    if (!checkAdminValues(productLink)) productLink = ProductLink()
                },
                enabled = productLink.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Product!")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AdminFormPreview() {
    FoodIOTheme(dynamicColor = false) {
        AdminForm()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AdminFormPreviewDark() {
    FoodIOTheme(dynamicColor = false, darkTheme = true) {
        AdminForm()
    }
}