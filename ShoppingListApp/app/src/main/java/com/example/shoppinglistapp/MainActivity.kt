package com.example.shoppinglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme

data class ShoppingItem(val name: String, var quantity: Int, var isChecked: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListAppTheme {
                ShoppingListScreen()
            }
        }
    }
}

@Composable
fun ShoppingListScreen() {
    var items by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var newItemName by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Input field for item name
        OutlinedTextField(
            value = newItemName,
            onValueChange = { newItemName = it },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Quantity adjustment buttons and display
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Quantity: $newItemQuantity")
            Spacer(modifier = Modifier.width(16.dp))

            // Add button to increase quantity
            Button(onClick = { newItemQuantity += 1 }) {
                Text("+")
            }


            // Minus button to decrease quantity
            Button(onClick = { if (newItemQuantity > 1) newItemQuantity -= 1 }) {
                Text("-")
            }

            Spacer(modifier = Modifier.width(8.dp))


        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to add new item to the shopping list
        Button(
            onClick = {
                if (newItemName.isNotEmpty()) {
                    items = items + ShoppingItem(name = newItemName, quantity = newItemQuantity)
                    newItemName = ""  // Clear the input field after adding the item
                    newItemQuantity = 1  // Reset the quantity to 1
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the list of shopping items
        LazyColumn {
            items(items.size) { index ->
                ShoppingItemCard(
                    item = items[index],
                    onQuantityChange = { quantity ->
                        items = items.toMutableList().also { it[index] = it[index].copy(quantity = quantity) }
                    },
                    onCheckedChange = { isChecked ->
                        items = items.toMutableList().also { it[index] = it[index].copy(isChecked = isChecked) }
                    },
                    onDelete = {
                        items = items.toMutableList().also { it.removeAt(index) }
                    }
                )
            }
        }
    }
}

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onQuantityChange: (Int) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox on the left to mark the item as checked or unchecked
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onCheckedChange(it) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(item.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Quantity: ${item.quantity}")

                    Spacer(modifier = Modifier.width(16.dp))

                    // Button to add 1 to the item's quantity
                    Button(onClick = { onQuantityChange(item.quantity + 1) }) {
                        Text("+")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Button to subtract 1 from the item's quantity
                    Button(onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }) {
                        Text("-")
                    }
                }
            }

            // Delete button to remove the item
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListPreview() {
    ShoppingListAppTheme {
        ShoppingListScreen()
    }
}
