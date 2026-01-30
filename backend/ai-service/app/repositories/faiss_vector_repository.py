import os
import shutil
import faiss
from langchain_community.docstore import InMemoryDocstore
from langchain_community.vectorstores import FAISS
from langchain_huggingface import HuggingFaceEmbeddings
from app.repositories.base_vector_repository import BaseVectorRepository

class FaissVectorRepository(BaseVectorRepository):
    def __init__(self, index_dir="faiss_index", index_file="index.faiss"):
        self.vectorstore = None
        self.index_dir = index_dir
        self.index_file = index_file
        self.embeddings = HuggingFaceEmbeddings(model_name="sentence-transformers/all-MiniLM-L6-v2")

    def get(self):
        if self.vectorstore is None:
            index_file = f"{self.index_dir}/{self.index_file}"
            if os.path.exists(index_file):
                self.vectorstore = FAISS.load_local(self.index_dir, self.embeddings, allow_dangerous_deserialization=True)
        return self.vectorstore

    def reset(self):
        self.vectorstore = None

    def delete_all(self):
        shutil.rmtree(self.index_dir, ignore_errors=True)
        self.reset()

    def initialize(self, text, metadata=None):
        vec = self.embeddings.embed_documents([text])[0]
        dim = len(vec)

        index = faiss.IndexFlatL2(dim)
        doc_store = InMemoryDocstore({})

        self.vectorstore = FAISS(
            embedding_function=self.embeddings,
            index=index,
            docstore=doc_store,
            index_to_docstore_id={}
        )

        self.vectorstore.add_texts([text], metadatas=[metadata or {}])
        self.vectorstore.save_local(self.index_dir)
        return self.vectorstore

    def rebuild(self, texts, metas):
        new_store = FAISS.from_texts(texts=texts, embedding=self.embeddings, metadatas=metas)
        new_store.save_local(self.index_dir)
        self.reset()