# -*- coding: utf-8 -*-
from PIL import Image
import os, glob, numpy as np
from keras.models import load_model
from keras.applications.inception_v3 import preprocess_input
import keras.backend as K
import flask
import io


# initialize our Flask application and the Keras model
app = flask.Flask(__name__)

image_w = 256
image_h = 256


def load_my_model():
    # data_dir = "./food-101/images/"
    # global foods_sorted
    # foods_sorted = sorted(os.listdir(data_dir))
    # print(foods_sorted)

    global foods_sorted
    foods_sorted = ['apple_pie', 'baby_back_ribs', 'baklava', 'beef_carpaccio', 'beef_tartare', 'beet_salad', 'beignets', 'bibimbap', 'bread_pudding', 'breakfast_burrito', 'bruschetta', 'caesar_salad', 'cannoli', 'caprese_salad', 'carrot_cake', 'ceviche', 'cheese_plate', 'cheesecake', 'chicken_curry', 'chicken_quesadilla', 'chicken_wings', 'chocolate_cake', 'chocolate_mousse', 'churros', 'clam_chowder', 'club_sandwich', 'crab_cakes', 'creme_brulee', 'croque_madame', 'cup_cakes', 'deviled_eggs', 'donuts', 'dumplings', 'edamame', 'eggs_benedict', 'escargots', 'falafel', 'filet_mignon', 'fish_and_chips', 'foie_gras', 'french_fries', 'french_onion_soup', 'french_toast', 'fried_calamari', 'fried_rice', 'frozen_yogurt', 'garlic_bread', 'gnocchi', 'greek_salad', 'grilled_cheese_sandwich', 'grilled_salmon', 'guacamole', 'gyoza', 'hamburger', 'hot_and_sour_soup', 'hot_dog', 'huevos_rancheros', 'hummus', 'ice_cream', 'lasagna', 'lobster_bisque', 'lobster_roll_sandwich', 'macaroni_and_cheese', 'macarons', 'miso_soup', 'mussels', 'nachos', 'omelette', 'onion_rings', 'oysters', 'pad_thai', 'paella', 'pancakes', 'panna_cotta', 'peking_duck', 'pho', 'pizza', 'pork_chop', 'poutine', 'prime_rib', 'pulled_pork_sandwich', 'ramen', 'ravioli', 'red_velvet_cake', 'risotto', 'samosa', 'sashimi', 'scallops', 'seaweed_salad', 'shrimp_and_grits', 'spaghetti_bolognese', 'spaghetti_carbonara', 'spring_rolls', 'steak', 'strawberry_shortcake', 'sushi', 'tacos', 'takoyaki', 'tiramisu', 'tuna_tartare', 'waffles']

    # Loading the saved model to make predictions
    K.clear_session()
    global model
    model = load_model('bestmodel_101class.hdf5', compile=False)
    print(" * InceptionV3 model loading is success")


@app.route("/predict", methods=["POST"])
def predict():
    # initialize the data dictionary that will be returned from the view
    response = {"success": False}

    # ensure an image was properly uploaded to our endpoint
    if flask.request.method == "POST":
        if flask.request.files.get("image"):
            # read the image in PIL format
            image = flask.request.files["image"].read()
            image = Image.open(io.BytesIO(image))
            image = image.convert("RGB")
            image = image.resize((image_w, image_h))

            image = np.expand_dims(image, axis=0)
            image = preprocess_input(image)

            pred = model.predict(image)
            index = np.argmax(pred)
            pred_value = foods_sorted[index]

            response["predictions"] = pred_value
            response["success"] = True

    # return the data dictionary as a JSON response
    return flask.jsonify(response)


# 메인함수
if __name__ == '__main__':
    print((" * Loading Keras InceptionV3 model and Flask starting server..."
           "please wait until server has fully started"))
    load_my_model()
    app.run(host='0.0.0.0')