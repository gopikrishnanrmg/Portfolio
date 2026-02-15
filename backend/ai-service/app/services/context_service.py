from app.repositories.vector_repository import vector_repo

from langchain_text_splitters import RecursiveCharacterTextSplitter
from app.dtos.context_dtos import CreateContextItemRequest

text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=800,
    chunk_overlap=200,
    separators=["\n\n", "\n", ".", " ", ""]
)

def add_context_item(item: CreateContextItemRequest):
    vectorstore = vector_repo.get()

    chunks = text_splitter.split_text(item.content)

    if vectorstore is None:
        vectorstore = vector_repo.initialize(
            chunks[0],
            {"title": item.title}
        )
        chunks = chunks[1:]

    if chunks:
        vectorstore.add_texts(
            texts=chunks,
            metadatas=[{"title": item.title}] * len(chunks)
        )
        vectorstore.save_local(vector_repo.index_dir)

    return len(chunks)

def find_k_matches(query: str, k: int):
    vectorstore = vector_repo.get()
    if vectorstore is None:
        return []
    return vectorstore.similarity_search(query, k)

def soft_delete_by_title(title: str):
    vectorstore = vector_repo.get()
    docs = vectorstore.docstore._dict

    remaining = [
        (doc.page_content, doc.metadata)
        for doc in docs.values()
        if doc.metadata.get("title") != title
    ]

    if not remaining:
        vector_repo.delete_all()
        return

    texts = [t for t, m in remaining]
    metas = [m for t, m in remaining]

    vector_repo.rebuild(texts, metas)
