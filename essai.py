import streamlit as st
import pandas as pd
import matplotlib.pyplot as plt

## En-tête de la Page
st.set_page_config(page_title="Droid Malware Detection Tool")

## Titre de la Page
st.title("Droid Malware Detection :green[Tool]")
st.markdown("By MEGNA MFOUAKIE Ibrahim (Master II's Computer Sciene Student at Univerty of Yaoundé I)")
st.subheader('', divider='rainbow')

## Add Widgets
st.sidebar.title("Droid-MalDec Tool")
st.sidebar.subheader("", divider='green')
st.sidebar.markdown("Simple (Right Now) Tool :computer: :computer: to predict if .apk file is malicious or benign")
st.sidebar.subheader("", divider='red')
uploaded_file = st.sidebar.file_uploader("Veuillez Choisir un Fichier S'il vous plaît :sunglasses:")
st.sidebar.subheader("", divider='grey')

if uploaded_file is not None:
  df = pd.read_csv(uploaded_file)
  st.write(df)
   # Add some matplotlib code !
  fig, ax = plt.subplots()
  df.hist(
    bins=8,
    column="android.permission.WRITE_EXTERNAL_STORAGE",
    grid=False,
    figsize=(8, 8),
    color="#86bf91",
    zorder=2,
    rwidth=0.9,
    ax=ax,
  )
  st.write(fig)

st.subheader('', divider='rainbow')
upload_file = st.file_uploader("Veuillez Choisir un Fichier (.apk) S'il vous plaît [En cours de conception]")

if upload_file is not None:
   pass
  




