from unittest.mock import MagicMock, patch
from dtos.context_dtos import CreateContextItemRequest
import pytest

@patch("services.context_service.vector_repo")
@patch("services.context_service.text_splitter")
def test_add_context_item_initializes_vectorstore(mock_splitter, mock_repo):
    mock_splitter.split_text.return_value = ["chunk1", "chunk2", "chunk3"]

    mock_repo.get.return_value = None

    mock_vectorstore = MagicMock()
    mock_repo.initialize.return_value = mock_vectorstore

    from services.context_service import add_context_item

    item = CreateContextItemRequest(title="Doc1", content="full text")

    count = add_context_item(item)

    mock_repo.initialize.assert_called_once_with("chunk1", {"title": "Doc1"})

    mock_vectorstore.add_texts.assert_called_once_with(
        texts=["chunk2", "chunk3"],
        metadatas=[{"title": "Doc1"}, {"title": "Doc1"}]
    )

    mock_vectorstore.save_local.assert_called_once_with(mock_repo.index_dir)

    assert count == 2

@patch("services.context_service.vector_repo")
@patch("services.context_service.text_splitter")
def test_add_context_item_existing_vectorstore(mock_splitter, mock_repo):
    mock_splitter.split_text.return_value = ["chunk1", "chunk2"]

    mock_vectorstore = MagicMock()
    mock_repo.get.return_value = mock_vectorstore

    from services.context_service import add_context_item

    item = CreateContextItemRequest(title="Doc1", content="full text")

    count = add_context_item(item)

    mock_repo.initialize.assert_not_called()

    mock_vectorstore.add_texts.assert_called_once_with(
        texts=["chunk1", "chunk2"],
        metadatas=[{"title": "Doc1"}, {"title": "Doc1"}]
    )

    mock_vectorstore.save_local.assert_called_once()

    assert count == 2

@patch("services.context_service.vector_repo")
@patch("services.context_service.text_splitter")
def test_add_context_item_existing_vectorstore(mock_splitter, mock_repo):
    mock_splitter.split_text.return_value = ["chunk1", "chunk2"]

    mock_vectorstore = MagicMock()
    mock_repo.get.return_value = mock_vectorstore

    from services.context_service import add_context_item

    item = CreateContextItemRequest(title="Doc1", content="full text")

    count = add_context_item(item)

    mock_repo.initialize.assert_not_called()

    mock_vectorstore.add_texts.assert_called_once_with(
        texts=["chunk1", "chunk2"],
        metadatas=[{"title": "Doc1"}, {"title": "Doc1"}]
    )

    mock_vectorstore.save_local.assert_called_once()

    assert count == 2

@patch("services.context_service.vector_repo")
def test_soft_delete_by_title_delete_all(mock_repo):
    mock_vectorstore = MagicMock()
    mock_repo.get.return_value = mock_vectorstore

    mock_vectorstore.docstore._dict = {
        "1": MagicMock(page_content="A", metadata={"title": "Doc1"}),
        "2": MagicMock(page_content="B", metadata={"title": "Doc1"}),
    }

    from services.context_service import soft_delete_by_title

    soft_delete_by_title("Doc1")

    mock_repo.delete_all.assert_called_once()
    mock_repo.rebuild.assert_not_called()

@patch("services.context_service.vector_repo")
def test_soft_delete_by_title_rebuild(mock_repo):
    mock_vectorstore = MagicMock()
    mock_repo.get.return_value = mock_vectorstore

    mock_vectorstore.docstore._dict = {
        "1": MagicMock(page_content="A", metadata={"title": "Doc1"}),
        "2": MagicMock(page_content="B", metadata={"title": "Doc2"}),
    }

    from services.context_service import soft_delete_by_title

    soft_delete_by_title("Doc1")

    mock_repo.delete_all.assert_not_called()

    mock_repo.rebuild.assert_called_once_with(
        ["B"],
        [{"title": "Doc2"}]
    )
